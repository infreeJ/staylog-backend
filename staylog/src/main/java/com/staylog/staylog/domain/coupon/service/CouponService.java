package com.staylog.staylog.domain.coupon.service;

import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.request.UseCouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;

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
     * 특정 유저의 이미 사용한 모든 쿠폰 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return CouponResponse[] 쿠폰 목록
     */
    public List<CouponResponse> getUnavailableCouponList(long userId);

    /**
     * 쿠폰 추가
     * @author 이준혁
     * @param couponRequest (userId, couponType)
     * @return 성공 시 1, 실패 시 0 반환
     */
    public void saveCoupon(CouponRequest couponRequest);



    /**
     * 쿠폰 사용 처리
     * @author 이준혁
     * @param useCouponRequest 쿠폰 PK
     * @return 성공 시 1, 실패 시 0 반환
     */
    public void useCoupon(UseCouponRequest useCouponRequest);

    /**
     * 쿠폰 삭제
     * @author 이준혁
     * @param couponId 쿠폰 PK
     * @return 성공 시 1, 실패 시 0 반환
     */
    public void deleteCoupon(long couponId);
}
