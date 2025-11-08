package com.staylog.staylog.domain.coupon.listener;

import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponCheckDto;
import com.staylog.staylog.domain.coupon.mapper.CouponMapper;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.PaymentConfirmEvent;
import com.staylog.staylog.global.event.ReviewCreatedEvent;
import com.staylog.staylog.global.event.SignupEvent;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponEventListener {

    private final CouponMapper couponMapper;
    private final CouponService couponService;


    /**
     * 리뷰글 작성 이벤트리스너 메서드(리뷰 쿠폰 발급)
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleReviewCreatedEvent(ReviewCreatedEvent event) {

        CouponRequest couponRequest = CouponRequest.builder()
                .userId(event.getUserId())
                .name("리뷰 쿠폰")
                .discount(5)
                .expiredAt(LocalDate.now().plusDays(30)) // 30일 후 만료
                .build();

        couponService.saveCoupon(couponRequest);
    }


    /**
     * 회원가입 이벤트리스너 메서드(환영 쿠폰 발급)
     * @param event 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handleSignupEvent(SignupEvent event) {

        CouponRequest couponRequest = CouponRequest.builder()
                .userId(event.getUserId())
                .name("회원가입 웰컴 쿠폰")
                .discount(5)
                .expiredAt(LocalDate.now().plusDays(30)) // 30일 후 만료
                .build();

        couponService.saveCoupon(couponRequest);
    }


    /**
     * 결제완료 이벤트리스너 메서드(쿠폰 사용 처리)
     *
     * @param event 결제 이벤트 객체
     * @author 이준혁
     */
    @TransactionalEventListener
    private void handlePaymentConfirmEvent(PaymentConfirmEvent event) {

        long couponId = event.getCouponId();
        CouponCheckDto couponCheckDto = couponMapper.checkAvailableCoupon(couponId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = couponCheckDto.getExpiredAt();
        boolean isNotExpired = (expiredAt == null) || (expiredAt.isAfter(now));

        int isSuccess = 0;
        if (couponCheckDto.getIsUsed().equals("N") && isNotExpired) {
            isSuccess = couponMapper.useCoupon(couponId);
        }

        if (isSuccess == 0) {
            log.warn("쿠폰 사용 실패: 만료 기간이 지났거나 이미 사용된 쿠폰입니다. - couponId={}", couponId);
            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
        }
    }
    

    // TODO: 결제 취소 시 쿠폰 미사용 처리 리스너 구현 필요
}
