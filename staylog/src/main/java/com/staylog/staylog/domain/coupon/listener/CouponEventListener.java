package com.staylog.staylog.domain.coupon.listener;

import com.staylog.staylog.domain.booking.mapper.BookingMapper;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.mapper.CouponMapper;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.global.annotation.CommonRetryable;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import com.staylog.staylog.global.event.ReviewCreatedEvent;
import com.staylog.staylog.global.event.SignupEvent;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    /**
     * 리뷰 쿠폰 발급(리뷰글 작성 이벤트리스너)
     *
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @Async
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleIssueReviewCoupon(ReviewCreatedEvent event) {

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
    @Async
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    public void handleIssueSignupCoupon(SignupEvent event) {

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
    // AFTER_COMMIT에 @Retryable을 사용해서 재시도할 예정이니 @Async로 비동기 처리 가능
    @Async
    @TransactionalEventListener
    @CommonRetryable // 실패시 재시도
    // TODO: @Recover 메서드 정의 필요
    public void handleProcessCouponUsage(PaymentConfirmEvent event) {
        if (event.getCouponId() == null) {
            log.warn("쿠폰 미사용 결제 건: paymentId={}", event.getPaymentId());
            return;
        }

        long userId = bookingMapper.findUserIdByBookingId(event.getBookingId());
        long couponId = event.getCouponId();

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


    // TODO: 결제 취소 시 쿠폰 미사용 처리 리스너 구현 필요
}
