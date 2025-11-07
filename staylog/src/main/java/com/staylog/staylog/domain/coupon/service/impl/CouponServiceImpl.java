package com.staylog.staylog.domain.coupon.service.impl;

import com.staylog.staylog.domain.coupon.dto.request.CouponBatchRequest;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;
import com.staylog.staylog.domain.coupon.mapper.CouponMapper;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.CouponCreatedEvent;
import com.staylog.staylog.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final ApplicationEventPublisher eventPublisher;



    /**
     * 특정 유저의 사용 가능한 모든 쿠폰 조회
     *
     * @param userId 유저 PK
     * @return List<CouponResponse> 쿠폰 목록
     * @author 이준혁
     */
    @Override
    public List<CouponResponse> getAvailableCouponList(long userId) {
        List<CouponResponse> list = couponMapper.getAvailableCouponList(userId);

        if (list == null) {
            log.warn("사용 가능한 쿠폰 데이터 조회 실패: 쿠폰이 없거나 잘못된 유저 데이터 - userId={}", userId);
            throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
        return list;
    }


    /**
     * 특정 유저의 사용 불가능한 모든 쿠폰 조회
     *
     * @param userId 유저 PK
     * @return List<CouponResponse> 쿠폰 목록
     * @author 이준혁
     */
    @Override
    public List<CouponResponse> getUnavailableCouponList(long userId) {
        List<CouponResponse> list = couponMapper.getUnavailableCouponList(userId);

        if (list == null) {
            log.warn("사용된 쿠폰 데이터 조회 실패: 쿠폰이 없거나 잘못된 유저 데이터 - userId={}", userId);
            throw new BusinessException(ErrorCode.COUPON_NOT_FOUND);
        }
        return list;
    }

    /**
     * 쿠폰 발급
     *
     * @param couponRequest (userId, couponType)
     * @author 이준혁
     */
    @Override
    @Transactional
    public void saveCoupon(CouponRequest couponRequest) {

        if(couponRequest.getExpiredAt() == null) {
            couponRequest.setExpiredAt(LocalDate.now().plusYears(100));
        }
        int isSuccess = couponMapper.saveCoupon(couponRequest);
        if (isSuccess == 0) {
            log.warn("쿠폰 생성 실패: 잘못된 요청입니다. - couponRequest={}", couponRequest);
            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
        }

        // =========== 쿠폰 발급 이벤트 발행(알림 전송) ==============
        CouponCreatedEvent event = new CouponCreatedEvent(couponRequest.getCouponId(), couponRequest.getUserId(), couponRequest.getName(), couponRequest.getDiscount());
        System.out.println("쿠폰 발급 이벤트 전송");
        eventPublisher.publishEvent(event);

    }

    /**
     * 모든 유저에게 쿠폰 일괄 발급
     *
     * @param couponBatchRequest couponBatchRequest Dto
     * @author 이준혁
     */
    @Override
    public void saveCouponToAllUsers(CouponBatchRequest couponBatchRequest) {
        int isSuccess = couponMapper.saveCouponToAllUsers(couponBatchRequest);

        if (isSuccess == 0) {
            log.warn("쿠폰 생성 실패: 잘못된 요청입니다. - couponBatchRequest={}", couponBatchRequest);
            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
        }

        // TODO: 쿠폰 발급 이벤트 발행 필요
    }


    /**
     * 쿠폰 삭제
     *
     * @param couponId 쿠폰 PK
     * @author 이준혁
     */
    @Override
    public void deleteCoupon(long couponId) {
        int isSuccess = couponMapper.deleteCoupon(couponId);
        if (isSuccess == 0) {
            log.warn("쿠폰 삭제 실패: 쿠폰 정보를 찾을 수 없습니다. - couponId={}", couponId);
            throw new BusinessException(ErrorCode.COUPON_FAILED_DELETED);
        }
    }
}
