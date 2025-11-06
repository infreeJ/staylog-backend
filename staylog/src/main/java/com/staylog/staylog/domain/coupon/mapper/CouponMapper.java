package com.staylog.staylog.domain.coupon.mapper;

import com.staylog.staylog.domain.coupon.dto.request.CouponBatchRequest;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponCheckDto;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {

    /**
     * 특정 유저의 사용 가능한 모든 쿠폰 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return List<CouponResponse> 쿠폰 목록
     */
    public List<CouponResponse> getAvailableCouponList(long userId);

    /**
     * 특정 유저의 사용 불가능한 모든 쿠폰 조회
     * @author 이준혁
     * @param userId 유저 PK
     * @return List<CouponResponse> 쿠폰 목록
     */
    public List<CouponResponse> getUnavailableCouponList(long userId);

    /**
     * 쿠폰 발급
     * @author 이준혁
     * @param couponRequest couponRequest Dto
     * @return 성공 시 1, 실패 시 0 반환
     */
    public int saveCoupon(CouponRequest couponRequest);

    /**
     * 모든 유저에게 쿠폰 일괄 발급
     * @author 이준혁
     * @param couponBatchRequest couponBatchRequest Dto
     * @return 성공 시 1, 실패 시 0 반환
     */
    public int saveCouponToAllUsers(CouponBatchRequest couponBatchRequest);

    /**
     * 쿠폰 사용 여부 확인
     * @author 이준혁
     * @param couponId 쿠폰 PK
     * @return CouponCheckDto is_used, expiredAt 반환
     */
    public CouponCheckDto checkAvailableCoupon(long couponId);

    /**
     * 쿠폰 사용 처리
     * @author 이준혁
     * @param couponId 쿠폰 PK
     * @return 성공 시 1, 실패 시 0 반환
     */
    public int useCoupon(long couponId);

    /**
     * 쿠폰 미사용 처리
     * @author 이준혁
     * @param couponId 쿠폰 PK
     * @return 성공 시 1, 실패 시 0 반환
     */
    public int unuseCoupon(long couponId);

    /**
     * 쿠폰 삭제
     * @author 이준혁
     * @param couponId 쿠폰 PK
     * @return 성공 시 1, 실패 시 0 반환
     */
    public int deleteCoupon(long couponId);
}
