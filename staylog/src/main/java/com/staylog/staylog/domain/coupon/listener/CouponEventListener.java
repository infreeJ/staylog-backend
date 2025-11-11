package com.staylog.staylog.domain.coupon.listener;

import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.mapper.CouponMapper;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.domain.payment.mapper.PaymentMapper;
import com.staylog.staylog.global.annotation.CommonRetryable;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import com.staylog.staylog.global.event.RefundConfirmEvent;
import com.staylog.staylog.global.event.ReviewCreatedEvent;
import com.staylog.staylog.global.event.SignupEvent;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponEventListener {

    private final CouponMapper couponMapper;
    private final CouponService couponService;
    private final BookingMapper bookingMapper;
    private final PaymentMapper paymentMapper;


    /**
     * 리뷰 쿠폰 발급(리뷰글 작성 이벤트리스너)
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleIssueReviewCoupon(ReviewCreatedEvent event) {
        log.info("handleCouponIssuanceNotification 리스너 실행. userId: {}, boardId: {}", event.getUserId(), event.getBoardId());

        CouponRequest couponRequest = CouponRequest.builder()
                .userId(event.getUserId())
                .name("리뷰 쿠폰")
                .discount(5)
                .expiredAt(LocalDate.now().plusDays(30)) // 30일 후 만료
                .build();

        couponService.saveCoupon(couponRequest);
    }


    /**
     * 환영 쿠폰 발급(회원가입 이벤트리스너)
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleIssueSignupCoupon(SignupEvent event) {
        log.info("handleIssueSignupCoupon 리스너 실행. userId: {}", event.getUserId());

        CouponRequest couponRequest = CouponRequest.builder()
                .userId(event.getUserId())
                .name("회원가입 웰컴 쿠폰")
                .discount(5)
                .expiredAt(LocalDate.now().plusDays(30)) // 30일 후 만료
                .build();

        couponService.saveCoupon(couponRequest);
    }


    /**
     * 쿠폰 사용 처리(결제완료 이벤트리스너)
     *
     * @param event 결제 이벤트 객체
     * @author 이준혁
     */
    // 결제 트랜잭션에 포함시키기 위해 BEFORE_COMMIT를 사용해서 결제와 쿠폰 사용의 원자성 보장하려 했으나
    // 쿠폰 사용이 실패해도 결제는 완료되는 것이 비즈니스 로직상 더 올바른 구조
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleProcessCouponUsage(PaymentConfirmEvent event) {
        if (event.getCouponId() == null) {
            log.warn("쿠폰 미사용 결제 건: paymentId={}", event.getPaymentId());
            return;
        }
        long userId = bookingMapper.findUserIdByBookingId(event.getBookingId());
        long couponId = event.getCouponId();
        log.info("handleProcessCouponUsage 리스너 실행. userId: {}, couponId: {}", userId, couponId);

        // 쿠폰 검증
        couponService.validateCoupon(userId, couponId);

        // 쿠폰 사용 처리
        int isSuccess = couponMapper.useCoupon(couponId);

        if (isSuccess == 0) {
            log.error("쿠폰 사용 처리 실패 (결제는 성공): couponId={}", couponId);
            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
        }

        log.info("쿠폰 사용 처리 완료: couponId={}", couponId);
    }

    /**
     * 쿠폰 미사용 처리(환불 완료 이벤트리스너)
     *
     * @param event 환불 이벤트 객체
     * @author 이준혁
     */
    @Async("asyncTaskExecutor")
    @TransactionalEventListener
    @CommonRetryable
    public void handleRevertCouponUsage(RefundConfirmEvent event) {
        Long couponId = paymentMapper.findCouponIdByPaymentId(event.getPaymentId());
        if (couponId == null) {
            log.warn("쿠폰 미사용 결제 건: paymentId={}", event.getPaymentId());
            return;
        }

        log.info("handleProcessCouponUsage 리스너 실행. refundId: {}, couponId: {}", event.getRefundId(), couponId);

        int isSuccess = couponMapper.unuseCoupon(couponId);
        if(isSuccess == 0) {
            log.error("쿠폰 미사용 처리 실패 (환불은 성공): couponId={}", couponId);
            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
        }
    }



    /**
     * Retryable 재시도 최종 실패 시 실행될 Recover 로직
     * @author 이준혁
     * @param t 예외 객체
     * @param event 실패한 이벤트 객체
     */
    @Recover
    public void recoverCouponOperations(Throwable t, Object event) {
        log.error("[Recover] 쿠폰 관련 작업 재시도 최종 실패. 원인: {}", t.getMessage(), t);

        if (event instanceof ReviewCreatedEvent rce) {
            log.error(" -> 실패 작업 상세: 리뷰 쿠폰 발급 (UserID: {}, BoardID: {})", rce.getUserId(), rce.getBoardId());

        } else if (event instanceof SignupEvent se) {
            log.error(" -> 실패 작업 상세: 회원가입 쿠폰 발급 (UserID: {})", se.getUserId());

        } else if (event instanceof PaymentConfirmEvent pce) {
            log.error(" -> 실패 작업 상세: 쿠폰 사용 처리 (PaymentID: {}, CouponID: {})", pce.getPaymentId(), pce.getCouponId());

        } else if(event instanceof RefundConfirmEvent pce) {
            log.error(" -> 실패 작업 상세: 쿠폰 미사용 처리 (PaymentID: {}, RefundId: {}, BookingId: {})",
                    pce.getPaymentId(), pce.getRefundId(), pce.getBookingId());

        } else {
            // 향후 추가될 쿠폰 관련 리스너를 위한 폴백
            log.error(" -> 실패 작업 상세: 알 수 없는 Event Type={}, Data={}", event.getClass().getSimpleName(), event);
        }

        // 에러 테이블 추가 시 DB 저장 필요
    }


}
