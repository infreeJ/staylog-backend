package com.staylog.staylog.domain.payment.service;

import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 결제 보상 트랜잭션 서비스
 * - 결제 실패 시 예약과 결제 상태를 롤백하는 보상 처리
 * - REQUIRES_NEW: 외부 트랜잭션과 독립적으로 실행되어 실패 시에도 커밋됨
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCompensationService {

    private final PaymentMapper paymentMapper;
    private final BookingMapper bookingMapper;

    /**
     * 결제 실패 시 보상 트랜잭션 실행
     * - PAYMENT -> PAY_FAILED
     * - RESERVATION -> RES_CANCELED
     *
     * REQUIRES_NEW 전파 속성:
     * - 외부 트랜잭션(confirmPayment)이 롤백되더라도 이 트랜잭션은 독립적으로 커밋됨
     * - 결제 실패 이력을 DB에 남기기 위해 필요
     *
     * @param bookingId 예약 ID
     * @param failureReason 실패 사유
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void compensateFailedPayment(Long bookingId, String failureReason) {
        log.warn("보상 트랜잭션 실행: bookingId={}, reason={}", bookingId, failureReason);

        try {
            // 1. PAYMENT 상태 -> FAILED
            Map<String, Object> payment = paymentMapper.findPaymentByBookingId(bookingId);
            if (payment != null) {
                Long paymentId = ((Number) payment.get("paymentId")).longValue();
                paymentMapper.updatePaymentFailure(paymentId, "PAY_FAILED", failureReason);
                log.debug("결제 상태 변경: paymentId={}, status=PAY_FAILED", paymentId);
            } else {
                log.warn("결제 정보 없음: bookingId={}", bookingId);
            }

            // 2. RESERVATION 상태 -> CANCELED
            bookingMapper.updateBookingStatus(bookingId, "RES_CANCELED");
            log.debug("예약 상태 변경: bookingId={}, status=RES_CANCELED", bookingId);

            log.info("보상 트랜잭션 커밋: bookingId={}", bookingId);

        } catch (Exception e) {
            log.error("보상 트랜잭션 실패: bookingId={}, error={}", bookingId, e.getMessage(), e);
            throw e;  // 보상 트랜잭션 롤백 (외부 트랜잭션과 무관)
        }
    }

    /**
     * 결제 만료 시 보상 트랜잭션 실행 (스케줄러용)
     * - PAYMENT -> PAY_EXPIRED
     * - RESERVATION -> RES_CANCELED
     *
     * @param bookingId 예약 ID
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void compensateExpiredPayment(Long bookingId) {
        log.warn("결제 만료 보상 트랜잭션 실행: bookingId={}", bookingId);

        try {
            // 1. PAYMENT 상태 -> EXPIRED (있는 경우에만)
            Map<String, Object> payment = paymentMapper.findPaymentByBookingId(bookingId);
            if (payment != null) {
                Long paymentId = ((Number) payment.get("paymentId")).longValue();
                String paymentStatus = (String) payment.get("status");

                // READY 상태인 결제만 EXPIRED로 변경
                if ("PAY_READY".equals(paymentStatus)) {
                    paymentMapper.updatePaymentFailure(paymentId, "PAY_EXPIRED", "결제 시간 만료");
                    log.debug("결제 상태 변경: paymentId={}, status=PAY_EXPIRED", paymentId);
                }
            }

            // 2. RESERVATION 상태 -> CANCELED
            bookingMapper.updateBookingStatus(bookingId, "RES_CANCELED");
            log.debug("예약 상태 변경: bookingId={}, status=RES_CANCELED", bookingId);

            log.info("만료 보상 트랜잭션 커밋: bookingId={}", bookingId);

        } catch (Exception e) {
            log.error("만료 보상 트랜잭션 실패: bookingId={}, error={}", bookingId, e.getMessage(), e);
            throw e;
        }
    }
}
