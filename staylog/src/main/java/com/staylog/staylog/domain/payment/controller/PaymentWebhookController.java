package com.staylog.staylog.domain.payment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.domain.booking.entity.Booking;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.payment.entity.Payment;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.external.toss.config.TossPaymentsConfig;
import com.staylog.staylog.external.toss.dto.request.TossVirtualAccountWebhookRequest;
import com.staylog.staylog.external.toss.dto.request.TossWebhookRequest;
import com.staylog.staylog.external.toss.dto.response.TossPaymentResponse;
import com.staylog.staylog.external.toss.dto.response.TossVirtualAccountResponse;
import com.staylog.staylog.global.constant.PaymentStatus;
import com.staylog.staylog.global.constant.ReservationStatus;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import com.staylog.staylog.global.util.WebhookSignatureValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * 결제 웹훅 API 컨트롤러
 * - Toss Payments 웹훅 처리
 * - 계좌이체 입금 확인 시 자동 결제 승인
 */
@Tag(name = "PaymentWebhookController", description = "결제 웹훅 API")
@RestController
@RequestMapping("/v1/payments/webhook")
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

    private final WebhookSignatureValidator signatureValidator;
    private final TossPaymentsConfig tossConfig;
    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Toss Payments 웹훅 수신
     * POST /v1/payments/webhook
     *
     * - 계좌이체 입금 확인 시 Toss에서 호출
     * - 시그니처 검증 후 결제 상태 업데이트
     *
     * @param payload 요청 본문 (raw JSON)
     * @param signature 웹훅 시그니처 (Toss-Signature 헤더)
     * @return 200 OK (웹훅 수신 확인)
     */
    @Operation(summary = "Toss 웹훅 수신", description = "Toss Payments 웹훅을 처리합니다. " +
            "계좌이체 입금 확인 시 결제를 자동으로 승인합니다.")
    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "Toss-Signature", required = false) String signature
    ) {
        log.info("========== 웹훅 수신 ==========");
        log.info("Payload: {}", payload);
        log.info("Signature: {}", signature);

        try {
            // 1. 시그니처 검증
            if (signature == null || !signatureValidator.validateSignature(
                    payload, signature, tossConfig.getWebhookSecret())) {
                log.warn("웹훅 시그니처 검증 실패");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
            }

            log.info("시그니처 검증 성공");

            // 2. eventType 확인 (v1 vs v2 구분)
            JsonNode rootNode = objectMapper.readTree(payload);
            String eventType = rootNode.path("eventType").asText();

            log.info("이벤트 타입: {}", eventType);

            // 3. v2 가상계좌 웹훅 처리
            if ("VirtualAccount.Deposit".equalsIgnoreCase(eventType)) {
                return handleVirtualAccountWebhook(payload);
            }

            // 4. v1 결제 웹훅 처리 (기존 로직)
            TossWebhookRequest webhookRequest = objectMapper.readValue(payload, TossWebhookRequest.class);
            TossPaymentResponse paymentData = webhookRequest.getData();

            log.info("웹훅 처리 시작: eventType={}, orderId={}, status={}",
                    webhookRequest.getEventType(), paymentData.getOrderId(), paymentData.getStatus());

            // 5. 결제 상태 확인 (DONE인 경우만 처리)
            if (!"DONE".equals(paymentData.getStatus())) {
                log.info("처리 불필요한 결제 상태: status={}", paymentData.getStatus());
                return ResponseEntity.ok("OK");
            }

            // 6. 예약 조회 (orderId = bookingNum)
            Booking booking = bookingMapper.findBookingByBookingNum(paymentData.getOrderId());
            if (booking == null) {
                log.warn("예약을 찾을 수 없음: orderId={}", paymentData.getOrderId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
            }

            Long bookingId = booking.getBookingId();

            // 7. 결제 조회
            Payment payment = paymentMapper.findPaymentByBookingId(bookingId);
            if (payment == null) {
                log.warn("결제를 찾을 수 없음: bookingId={}", bookingId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }

            Long paymentId = payment.getPaymentId();
            String currentStatus = payment.getStatus();

            // 8. 이미 처리된 결제인지 확인 (멱등성)
            if (PaymentStatus.PAY_PAID.getCode().equals(currentStatus)) {
                log.info("이미 처리된 결제: paymentId={}", paymentId);
                return ResponseEntity.ok("Already processed");
            }

            // 9. 결제 승인: PAYMENT(PAID) + RESERVATION(CONFIRMED)
            paymentMapper.updatePaymentApproved(
                    paymentId,
                    PaymentStatus.PAY_PAID.getCode(),
                    paymentData.getPaymentKey(),
                    paymentData.getLastTransactionKey()
            );

            bookingMapper.updateBookingStatus(bookingId, ReservationStatus.RES_CONFIRMED.getCode());

            log.info("웹훅 처리 완료: paymentId={}, bookingId={}", paymentId, bookingId);

            // 10. 결제 완료 이벤트 발행 (알림 전송 / 쿠폰 사용처리)
            PaymentConfirmEvent event = new PaymentConfirmEvent(
                    paymentId,
                    bookingId,
                    payment.getAmount(),
                    payment.getCouponId()
            );
            eventPublisher.publishEvent(event);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            log.error("웹훅 처리 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    /**
     * v2 가상계좌 웹훅 처리
     * - 가상계좌 입금 완료 시 호출됨
     *
     * @param payload 웹훅 페이로드
     * @return 200 OK
     */
    private ResponseEntity<String> handleVirtualAccountWebhook(String payload) {
        try {
            log.info("========== v2 가상계좌 웹훅 처리 시작 ==========");

            // 1. JSON 파싱
            TossVirtualAccountWebhookRequest webhookRequest = objectMapper.readValue(
                    payload, TossVirtualAccountWebhookRequest.class);
            TossVirtualAccountResponse vaData = webhookRequest.getData();

            log.info("가상계좌 웹훅: eventType={}, orderId={}, status={}",
                    webhookRequest.getEventType(), vaData.getOrderId(), vaData.getStatus());

            // 2. 입금 완료 상태 확인 (DONE인 경우만 처리)
            if (!"DONE".equals(vaData.getStatus())) {
                log.info("처리 불필요한 가상계좌 상태: status={}", vaData.getStatus());
                return ResponseEntity.ok("OK");
            }

            // 3. 예약 조회 (orderId = bookingNum)
            Booking booking = bookingMapper.findBookingByBookingNum(vaData.getOrderId());
            if (booking == null) {
                log.warn("예약을 찾을 수 없음: orderId={}", vaData.getOrderId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
            }

            Long bookingId = booking.getBookingId();

            // 4. 결제 조회
            Payment payment = paymentMapper.findPaymentByBookingId(bookingId);
            if (payment == null) {
                log.warn("결제를 찾을 수 없음: bookingId={}", bookingId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }

            Long paymentId = payment.getPaymentId();
            String currentStatus = payment.getStatus();

            // 5. 이미 처리된 결제인지 확인 (멱등성)
            if (PaymentStatus.PAY_PAID.getCode().equals(currentStatus)) {
                log.info("이미 처리된 가상계좌 입금: paymentId={}", paymentId);
                return ResponseEntity.ok("Already processed");
            }

            // 6. 결제 승인: PAYMENT(PAID) + RESERVATION(CONFIRMED)
            paymentMapper.updateVirtualAccountDeposit(
                    paymentId,
                    PaymentStatus.PAY_PAID.getCode(),
                    OffsetDateTime.now()
            );

            bookingMapper.updateBookingStatus(bookingId, ReservationStatus.RES_CONFIRMED.getCode());

            log.info("v2 가상계좌 웹훅 처리 완료: paymentId={}, bookingId={}", paymentId, bookingId);

            // 7. 결제 완료 이벤트 발행 (알림 전송 / 쿠폰 사용처리)
            PaymentConfirmEvent event = new PaymentConfirmEvent(
                    paymentId,
                    bookingId,
                    payment.getAmount(),
                    payment.getCouponId()
            );
            eventPublisher.publishEvent(event);

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            log.error("v2 가상계좌 웹훅 처리 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
}
