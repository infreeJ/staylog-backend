package com.staylog.staylog.domain.payment.service.impl;

import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.booking.service.BookingService;
import com.staylog.staylog.domain.payment.dto.request.ConfirmPaymentRequest;
import com.staylog.staylog.domain.payment.dto.request.PreparePaymentRequest;
import com.staylog.staylog.domain.payment.dto.response.PaymentResultResponse;
import com.staylog.staylog.domain.payment.dto.response.PreparePaymentResponse;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.domain.payment.service.PaymentCompensationService;
import com.staylog.staylog.domain.payment.service.PaymentService;
import com.staylog.staylog.external.toss.client.TossPaymentClient;
import com.staylog.staylog.external.toss.config.TossPaymentsConfig;
import com.staylog.staylog.external.toss.dto.request.TossConfirmRequest;
import com.staylog.staylog.external.toss.dto.response.TossPaymentResponse;
import com.staylog.staylog.global.exception.custom.booking.BookingNotFoundException;
import com.staylog.staylog.global.exception.custom.payment.PaymentAmountMismatchException;
import com.staylog.staylog.global.exception.custom.payment.PaymentFailedException;
import com.staylog.staylog.global.exception.custom.payment.PaymentNotFoundException;
import com.staylog.staylog.global.exception.custom.payment.TossApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 결제 서비스 구현
 * - 결제 준비 (READY 상태)
 * - 결제 승인 (Toss API 호출)
 * - 보상 트랜잭션 (실패 시 롤백)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;
    private final TossPaymentClient tossPaymentClient;
    private final TossPaymentsConfig tossConfig;
    private final PaymentCompensationService compensationService;

    /**
     * 결제 준비
     * - 예약 상태 검증 (PENDING & 5분 이내)
     * - 결제 생성 (READY 상태)
     */
    @Override
    @Transactional
    public PreparePaymentResponse preparePayment(PreparePaymentRequest request) {
        log.info("결제 준비 시작: bookingId={}, amount={}", request.getBookingId(), request.getAmount());

        // 1. 예약 상태 검증 (PENDING & 5분 이내)
        bookingService.validateBookingPending(request.getBookingId());

        // 2. 예약 정보 조회
        Map<String, Object> booking = bookingMapper.findBookingById(request.getBookingId());
        if (booking == null) {
            throw new BookingNotFoundException(request.getBookingId());
        }

        Long bookingAmount = ((Number) booking.get("amount")).longValue();
        String bookingNum = (String) booking.get("bookingNum");
        String guestName = (String) booking.get("guestName");

        // 3. 금액 검증
        if (!bookingAmount.equals(request.getAmount())) {
            log.error("결제 금액 불일치: 예약금액={}, 요청금액={}", bookingAmount, request.getAmount());
            throw new PaymentAmountMismatchException(bookingAmount, request.getAmount());
        }

        // 4. 계좌이체인 경우 만료 시간 연장 (5분 → 7일)
        if ("TRANSFER".equals(request.getMethod())) {
            LocalDateTime newExpiresAt = LocalDateTime.now().plusDays(7);
            bookingMapper.updateExpiresAt(request.getBookingId(), newExpiresAt);
            log.info("계좌이체 예약 만료 시간 연장: bookingId={}, expiresAt={}", request.getBookingId(), newExpiresAt);
        }

        // 5. 결제 생성 (READY 상태)
        Map<String, Object> params = new HashMap<>();
        params.put("status", "PAY_READY");  // READY 상태
        params.put("amount", request.getAmount());
        params.put("method", request.getMethod());
        params.put("bookingId", request.getBookingId());
        params.put("paymentKey", "");  // 아직 없음 (Toss 승인 시 업데이트)

        paymentMapper.insertPayment(params);

        // 6. 생성된 결제 조회
        Long paymentId = (Long) params.get("paymentId");
        Map<String, Object> payment = paymentMapper.findPaymentById(paymentId);

        log.info("결제 준비 완료: paymentId={}, orderId={}, method={}", paymentId, bookingNum, request.getMethod());

        return PreparePaymentResponse.builder()
                .paymentId(paymentId)
                .orderId(bookingNum)  // Toss에 전달할 주문번호
                .amount(request.getAmount())
                .method(request.getMethod())
                .clientKey(tossConfig.getClientKey())  // 프론트엔드용
                .customerName(guestName)
                .build();
    }

    /**
     * 결제 승인
     * - Toss API 호출
     * - 성공: PAYMENT(PAID) + RESERVATION(CONFIRMED)
     * - 실패: 보상 트랜잭션 (PAYMENT(FAILED) + RESERVATION(CANCELED))
     */
    @Override
    @Transactional
    public PaymentResultResponse confirmPayment(ConfirmPaymentRequest request) {
        log.info("결제 승인 시작: paymentKey={}, orderId={}, amount={}",
                request.getPaymentKey(), request.getOrderId(), request.getAmount());

        // 1. 예약 조회 (orderId = bookingNum)
        Map<String, Object> booking = bookingMapper.findBookingByBookingNum(request.getOrderId());
        if (booking == null) {
            throw new PaymentFailedException("예약을 찾을 수 없습니다: " + request.getOrderId());
        }

        Long bookingId = ((Number) booking.get("bookingId")).longValue();
        Long bookingAmount = ((Number) booking.get("amount")).longValue();

        // 2. 금액 검증
        if (!bookingAmount.equals(request.getAmount())) {
            log.error("결제 금액 불일치: 예약금액={}, 요청금액={}", bookingAmount, request.getAmount());

            // 보상 트랜잭션 실행 (독립 트랜잭션)
            compensationService.compensateFailedPayment(bookingId, "결제 금액 불일치");

            throw new PaymentAmountMismatchException(bookingAmount, request.getAmount());
        }

        // 3. 결제 조회
        Map<String, Object> payment = paymentMapper.findPaymentByBookingId(bookingId);
        if (payment == null) {
            throw new PaymentFailedException("결제 정보를 찾을 수 없습니다");
        }

        Long paymentId = ((Number) payment.get("paymentId")).longValue();

        try {
            // 4. Toss API 결제 승인 호출
            TossConfirmRequest tossRequest = TossConfirmRequest.builder()
                    .paymentKey(request.getPaymentKey())
                    .orderId(request.getOrderId())
                    .amount(request.getAmount())
                    .build();

            TossPaymentResponse tossResponse = tossPaymentClient.confirm(tossRequest);

            // 5. 성공: PAYMENT(PAID) + RESERVATION(CONFIRMED)
            paymentMapper.updatePaymentStatus(
                    paymentId,
                    "PAY_PAID",  // PAID 상태
                    tossResponse.getPaymentKey(),
                    tossResponse.getLastTransactionKey()
            );

            bookingMapper.updateBookingStatus(bookingId, "RES_CONFIRMED");  // CONFIRMED 상태

            log.info("결제 승인 성공: paymentId={}, bookingId={}", paymentId, bookingId);

            return PaymentResultResponse.builder()
                    .paymentId(paymentId)
                    .paymentKey(tossResponse.getPaymentKey())
                    .orderId(tossResponse.getOrderId())
                    .amount(tossResponse.getTotalAmount())
                    .method(tossResponse.getMethod())
                    .paymentStatus("PAY_PAID")
                    .reservationStatus("RES_CONFIRMED")
                    .requestedAt((LocalDateTime) payment.get("requestedAt"))
                    .approvedAt(tossResponse.getApprovedAt())
                    .build();

        } catch (TossApiException e) {
            // 6. 실패: 보상 트랜잭션 (PAYMENT(FAILED) + RESERVATION(CANCELED))
            log.error("Toss 결제 승인 실패: {}", e.getMessage(), e);

            // 독립적인 트랜잭션으로 보상 실행 (REQUIRES_NEW)
            compensationService.compensateFailedPayment(bookingId, e.getTossErrorMessage());

            throw e;  // 호출자에게 예외 전파
        } catch (Exception e) {
            // 7. 기타 예외: 보상 트랜잭션
            log.error("결제 승인 중 예외 발생: {}", e.getMessage(), e);

            // 독립적인 트랜잭션으로 보상 실행 (REQUIRES_NEW)
            compensationService.compensateFailedPayment(bookingId, "결제 처리 중 오류 발생");

            throw new PaymentFailedException("결제 처리 중 오류가 발생했습니다");
        }
    }
}
