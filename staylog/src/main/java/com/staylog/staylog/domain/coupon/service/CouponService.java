package com.staylog.staylog.domain.coupon.service;

import com.staylog.staylog.domain.coupon.dto.request.CouponBatchRequest;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.request.UseCouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponDiscountResult;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;
import com.staylog.staylog.global.event.SignupEvent;

import java.util.List;

public interface CouponService {

    /**
     * 특정 유저의 사용 가능한 모든 쿠폰 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return CouponResponse[] 쿠폰 목록
     */
    public List<CouponResponse> getAvailableCouponList(long userId);

    /**
     * 특정 유저의 사용 불가능한 모든 쿠폰 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return CouponResponse[] 쿠폰 목록
     */
    public List<CouponResponse> getUnavailableCouponList(long userId);

    /**
     * 쿠폰 발급
     * @author 이준혁
     * @param couponRequest couponRequest Dto
     * @return 성공 시 1, 실패 시 0 반환
     */
    public void saveCoupon(CouponRequest couponRequest);

    /**
     * 모든 유저에게 쿠폰 일괄 발급
     * @author 이준혁
     * @param couponBatchRequest couponBatchRequest Dto
     */
    public void saveCouponToAllUsers(CouponBatchRequest couponBatchRequest);

    /**
     * 쿠폰 삭제
     * @author 이준혁
     * @param couponId 쿠폰 PK
     * @return 성공 시 1, 실패 시 0 반환
     */
    public void deleteCoupon(long couponId);

    /**
     * 쿠폰 검증 및 퍼센트 할인 계산
     * - 쿠폰 소유자 검증
     * - 쿠폰 사용 가능 여부 검증 (사용 여부, 만료일)
     * - 퍼센트 할인 계산 (DISCOUNT 컬럼 값을 %로 해석)
     *
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     * @return CouponDiscountResult 할인 계산 결과 (원래 금액, 할인액, 최종 금액)
     * @author danjae
     */
    public CouponResponse validateCoupon(Long userId, Long couponId);

//    /**
//     * 쿠폰 사용 처리 (결제 승인 성공 시 호출)
//     * - is_used = 'Y'
//     * - used_at = 현재 시간
//     *
//     * @param couponId 쿠폰 ID
//     * @throws BusinessException 쿠폰 사용 처리 실패 시
//     * @author danjae
//     */
//    public void applyCouponUsage(Long couponId);


    public Long calculateCouponDiscount(Long originalAmount, int discountPercent);

    /**
     * 쿠폰 복구 처리 (결제 실패/취소 시 호출)
     * - is_used = 'N'
     * - used_at = NULL
     *
     * @param couponId 쿠폰 ID
     * @author danjae
     */
    public void revertCouponUsage(Long couponId);
}
