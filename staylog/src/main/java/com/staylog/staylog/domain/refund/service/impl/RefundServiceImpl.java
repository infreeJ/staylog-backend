package com.staylog.staylog.domain.refund.service.impl;

import com.staylog.staylog.domain.booking.dto.response.BookingDetailResponse;
import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.payment.entity.Payment;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.domain.refund.dto.request.RequestRefundRequest;
import com.staylog.staylog.domain.refund.dto.response.RefundDetailResponse;
import com.staylog.staylog.domain.refund.dto.response.RefundResultResponse;
import com.staylog.staylog.domain.refund.mapper.RefundMapper;
import com.staylog.staylog.domain.refund.service.RefundService;
import com.staylog.staylog.external.toss.client.TossPaymentClient;
import com.staylog.staylog.external.toss.dto.request.TossCancelRequest;
import com.staylog.staylog.external.toss.dto.response.TossPaymentResponse;
import com.staylog.staylog.global.constant.RefundType;
import com.staylog.staylog.global.constant.ReservationStatus;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import com.staylog.staylog.global.event.RefundConfirmEvent;
import com.staylog.staylog.global.exception.custom.booking.BookingNotFoundException;
import com.staylog.staylog.global.exception.custom.payment.PaymentNotFoundException;
import com.staylog.staylog.global.exception.custom.refund.RefundFailedException;
import com.staylog.staylog.global.exception.custom.refund.RefundPolicyViolationException;
import com.staylog.staylog.global.util.RefundPolicyCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 환불 서비스 구현
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefundServiceImpl implements RefundService {

    private final RefundMapper refundMapper;
    private final BookingMapper bookingMapper;
    private final PaymentMapper paymentMapper;
    private final TossPaymentClient tossPaymentClient;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 환불 요청
     * - 예약 취소 및 환불 생성
     */
    @Override
    @Transactional
    public RefundDetailResponse requestRefund(Long userId, RequestRefundRequest request) {
        log.info("환불 요청 시작: userId={}, bookingId={}", userId, request.getBookingId());

        // 1. 예약 조회 및 검증
        BookingDetailResponse booking = bookingMapper.findBookingById(request.getBookingId());
        if (booking == null) {
            throw new BookingNotFoundException(request.getBookingId());
        }

        Long bookingUserId = booking.getAmount();
        String bookingStatus = booking.getStatus();
        LocalDate checkInDate = booking.getCheckIn();

        // 본인 예약 확인
        if (!bookingUserId.equals(userId)) {
            throw new RefundPolicyViolationException("본인의 예약만 취소할 수 있습니다");
        }

        ReservationStatus status = ReservationStatus.fromCode(booking.getStatus());

        // 예약 상태 확인 (CONFIRMED만 환불 가능)
        if ((status != ReservationStatus.RES_CONFIRMED)) {
            throw new RefundPolicyViolationException("확정된 예약만 취소할 수 있습니다: " + bookingStatus);
        }

        // 2. 결제 정보 조회
        Payment payment = paymentMapper.findPaymentByBookingId(request.getBookingId());
        if (payment == null) {
            throw new PaymentNotFoundException("bookingId", request.getBookingId());
        }

        Long paymentId = payment.getPaymentId();
        Long totalAmount = payment.getAmount();
        String paymentStatus = payment.getStatus();

        // 결제 상태 확인 (PAID만 환불 가능)
        if (!"PAY_PAID".equals(paymentStatus)) {
            throw new RefundPolicyViolationException("결제 완료된 예약만 취소할 수 있습니다: " + paymentStatus);
        }

        // 3. 환불 정책 계산
        LocalDate today = LocalDate.now();
        RefundType refundType = RefundPolicyCalculator.calculateRefundType(checkInDate, today);

        // 환불 가능 여부 확인
        if (!RefundPolicyCalculator.isRefundable(checkInDate, today)) {
            throw new RefundPolicyViolationException("당일 취소는 환불이 불가능합니다");
        }

        Long refundAmount = RefundPolicyCalculator.calculateRefundAmount(totalAmount, refundType);
        String policyMessage = RefundPolicyCalculator.getRefundPolicyMessage(refundType);

        log.info("환불 정책 적용: refundType={}, totalAmount={}, refundAmount={}",
                refundType.getCode(), totalAmount, refundAmount);

        // 4. 환불 생성
        Map<String, Object> params = new HashMap<>();
        params.put("paymentId", paymentId);
        params.put("bookingId", request.getBookingId());
        params.put("totalAmount", totalAmount);
        params.put("refundAmount", refundAmount);
        params.put("refundType", refundType.getCode());
        params.put("refundReason", request.getRefundReason());
        params.put("status", "REFUND_REQUESTED");

        refundMapper.insertRefund(params);

        Long refundId = (Long) params.get("refundId");
        log.info("환불 생성 완료: refundId={}", refundId);

        // 5. 예약 상태 변경 (RES_REFUND_REQUESTED)
        bookingMapper.updateBookingStatus(request.getBookingId(), "RES_REFUND_REQUESTED");

        // 6. 생성된 환불 조회
        Map<String, Object> refund = refundMapper.findRefundById(refundId);

        return mapToRefundDetailResponse(refund, policyMessage);
    }

    /**
     * 환불 처리 (Toss API 호출)
     */
    @Override
    @Transactional
    public RefundResultResponse processRefund(Long refundId) {
        log.info("환불 처리 시작: refundId={}", refundId);

        // 1. 환불 정보 조회
        Map<String, Object> refund = refundMapper.findRefundById(refundId);
        if (refund == null) {
            throw new RefundFailedException("환불 정보를 찾을 수 없습니다: refundId=" + refundId);
        }

        Long paymentId = ((Number) refund.get("paymentId")).longValue();
        Long bookingId = ((Number) refund.get("bookingId")).longValue();
        Long refundAmount = ((Number) refund.get("refundAmount")).longValue();
        String refundReason = (String) refund.get("refundReason");
        String paymentKey = (String) refund.get("paymentKey");

        try {
            // 2. Toss 결제 취소 API 호출
            TossCancelRequest cancelRequest = TossCancelRequest.builder()
                    .cancelReason(refundReason)
                    .cancelAmount(refundAmount)
                    .build();

            TossPaymentResponse tossResponse = tossPaymentClient.cancel(paymentKey, cancelRequest);

            // 3. 성공: REFUND(COMPLETED) + PAYMENT(PAY_REFUND) + RESERVATION(RES_REFUNDED)
            refundMapper.updateRefundCompletion(refundId, "REFUND_COMPLETED");
            paymentMapper.updatePaymentApproved(paymentId, "PAY_REFUND", paymentKey, null);
            bookingMapper.updateBookingStatus(bookingId, "RES_REFUNDED");

            log.info("환불 처리 성공: refundId={}, paymentKey={}", refundId, paymentKey);

            // ============ 환불 완료 이벤트 발행(알림 전송 / 쿠폰 미사용처리) =============
            RefundConfirmEvent event = new RefundConfirmEvent(refundId, paymentId, bookingId, refundAmount);
            eventPublisher.publishEvent(event);
            // ==========================================================

            return RefundResultResponse.builder()
                    .refundId(refundId)
                    .paymentId(paymentId)
                    .paymentKey(paymentKey)
                    .refundAmount(refundAmount)
                    .refundType((String) refund.get("refundType"))
                    .refundReason(refundReason)
                    .refundStatus("REFUND_COMPLETED")
                    .paymentStatus("PAY_REFUND")
                    .bookingStatus("RES_REFUNDED")
                    .requestedAt((OffsetDateTime) refund.get("requestedAt"))
                    .completedAt(OffsetDateTime.now())
                    .build();

        } catch (Exception e) {
            // 4. 실패: REFUND(FAILED)
            log.error("Toss 환불 실패: refundId={}, error={}", refundId, e.getMessage(), e);

            refundMapper.updateRefundFailure(refundId, "REFUND_FAILED", e.getMessage());

            throw new RefundFailedException("환불 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 환불 조회
     */
    @Override
    @Transactional(readOnly = true)
    public RefundDetailResponse getRefund(Long refundId) {
        Map<String, Object> refund = refundMapper.findRefundById(refundId);

        if (refund == null) {
            throw new RefundFailedException("환불 정보를 찾을 수 없습니다: refundId=" + refundId);
        }

        String refundTypeCode = (String) refund.get("refundType");
        RefundType refundType = RefundType.fromCode(refundTypeCode);
        String policyMessage = RefundPolicyCalculator.getRefundPolicyMessage(refundType);

        return mapToRefundDetailResponse(refund, policyMessage);
    }

    /**
     * Map -> RefundDetailResponse 변환
     */
    private RefundDetailResponse mapToRefundDetailResponse(Map<String, Object> refund, String policyMessage) {
        return RefundDetailResponse.builder()
                .refundId(((Number) refund.get("refundId")).longValue())
                .paymentId(((Number) refund.get("paymentId")).longValue())
                .bookingId(((Number) refund.get("bookingId")).longValue())
                .bookingNum((String) refund.get("bookingNum"))
                .totalAmount(((Number) refund.get("totalAmount")).longValue())
                .refundAmount(((Number) refund.get("refundAmount")).longValue())
                .refundType((String) refund.get("refundType"))
                .refundReason((String) refund.get("refundReason"))
                .status((String) refund.get("status"))
                .paymentStatus((String) refund.get("paymentStatus"))
                .bookingStatus((String) refund.get("bookingStatus"))
                .checkInDate((LocalDate) refund.get("checkInDate"))
                .requestedAt((OffsetDateTime) refund.get("requestedAt"))
                .approvedAt((OffsetDateTime) refund.get("approvedAt"))
                .completedAt((OffsetDateTime) refund.get("completedAt"))
                .failureReason((String) refund.get("failureReason"))
                .policyMessage(policyMessage)
                .build();
    }
}
