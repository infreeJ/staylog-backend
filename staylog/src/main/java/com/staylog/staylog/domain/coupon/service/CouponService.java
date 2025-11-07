package com.staylog.staylog.domain.coupon.service;

import com.staylog.staylog.domain.coupon.dto.request.CouponBatchRequest;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.request.UseCouponRequest;
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
}
