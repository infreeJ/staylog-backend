package com.staylog.staylog.domain.payment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staylog.staylog.domain.booking.entity.Booking;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.payment.entity.Payment;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.external.toss.config.TossPaymentsConfig;
import com.staylog.staylog.external.toss.dto.request.TossVirtualAccountWebhookRequest;
import com.staylog.staylog.external.toss.dto.response.TossVirtualAccountResponse;
import com.staylog.staylog.global.constant.PaymentStatus;
import com.staylog.staylog.global.constant.ReservationStatus;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/v2/payments/webhook")
@RequiredArgsConstructor
@Slf4j
public class PaymentWebhookController {

    private final TossPaymentsConfig tossConfig;
    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * v2 가상계좌 웹훅 수신
     */
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        log.info("========== v2 가상계좌 웹훅 수신 ==========");
        log.info("Payload: {}", payload);

        try {
            // 1. payload JSON 파싱
            JsonNode rootNode = objectMapper.readTree(payload);

            // 2. status 확인
            String status = rootNode.path("status").asText();
            log.info("입금 상태: {}", status);

            if (!"DONE".equalsIgnoreCase(status)) {
                log.info("처리 불필요한 이벤트: {}", status);
                return ResponseEntity.ok("OK");
            }

            // 3. secret 검증
            String secretInPayload = rootNode.path("secret").asText();
            if (!tossConfig.getWebhookSecret().equals(secretInPayload)) {
                log.warn("웹훅 시크릿 검증 실패: {}", secretInPayload);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret");
            }
            log.info("시크릿 검증 성공");

            // 4. DTO 변환
            TossVirtualAccountWebhookRequest webhookRequest = objectMapper.readValue(payload,
                    TossVirtualAccountWebhookRequest.class);
            TossVirtualAccountResponse vaData = webhookRequest.getData();

            // 5. 입금 상태 확인
            if (!"DONE".equals(vaData.getStatus())) {
                log.info("처리 불필요한 가상계좌 상태: status={}", vaData.getStatus());
                return ResponseEntity.ok("OK");
            }

            // 6. 예약 조회
            Booking booking = bookingMapper.findBookingByBookingNum(vaData.getOrderId());
            if (booking == null) {
                log.warn("예약을 찾을 수 없음: orderId={}", vaData.getOrderId());
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
                log.info("이미 처리된 가상계좌 입금: paymentId={}", paymentId);
                return ResponseEntity.ok("Already processed");
            }

            // 9. 결제 승인: PAYMENT(PAID) + RESERVATION(CONFIRMED)
            paymentMapper.updateVirtualAccountDeposit(paymentId,
                    PaymentStatus.PAY_PAID.getCode(),
                    OffsetDateTime.now());

            bookingMapper.updateBookingStatus(bookingId, ReservationStatus.RES_CONFIRMED.getCode());
            log.info("v2 가상계좌 웹훅 처리 완료: paymentId={}, bookingId={}", paymentId, bookingId);

            // 10. 결제 완료 이벤트 발행
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
