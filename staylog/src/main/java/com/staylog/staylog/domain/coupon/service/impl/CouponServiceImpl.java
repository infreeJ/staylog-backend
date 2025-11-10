package com.staylog.staylog.domain.coupon.service.impl;

import com.staylog.staylog.domain.coupon.dto.request.CouponBatchRequest;
import com.staylog.staylog.domain.coupon.dto.request.CouponRequest;
import com.staylog.staylog.domain.coupon.dto.response.CouponResponse;
import com.staylog.staylog.domain.coupon.mapper.CouponMapper;
import com.staylog.staylog.domain.coupon.service.CouponService;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.event.CouponCreatedAllEvent;
import com.staylog.staylog.global.event.CouponCreatedEvent;
import com.staylog.staylog.global.exception.BusinessException;
import com.staylog.staylog.global.exception.custom.NotFoundException;
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
        eventPublisher.publishEvent(event);
    }

    /**
     * 모든 유저에게 쿠폰 일괄 발급
     *
     * @param couponBatchRequest couponBatchRequest Dto
     * @author 이준혁
     */
    @Override
    @Transactional
    public void saveCouponToAllUsers(CouponBatchRequest couponBatchRequest) {
        int isSuccess = couponMapper.saveCouponToAllUsers(couponBatchRequest);

        if (isSuccess == 0) {
            log.warn("쿠폰 생성 실패: 잘못된 요청입니다. - couponBatchRequest={}", couponBatchRequest);
            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
        }

        // =========== 쿠폰 발급 이벤트 발행(알림 전송) ==============
        CouponCreatedAllEvent event = new CouponCreatedAllEvent(couponBatchRequest.getName(), couponBatchRequest.getDiscount());
        eventPublisher.publishEvent(event);
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


    /**
     * 쿠폰 검증
     * - PaymentService에서 결제 준비 시 호출
     *
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     * @return coupon 쿠폰 객체
     * @author danjae
     */
    @Override
    public CouponResponse validateCoupon(Long userId, Long couponId) {
        
        log.info("쿠폰 검증 및 할인 계산 시작: userId={}, couponId={}", userId, couponId);
        CouponResponse coupon = couponMapper.getByCouponId(couponId);

        // 쿠폰 소유자 검증
        if (coupon.getUserId() != userId) {
            log.warn("쿠폰 소유자 불일치: couponUserId={}, requestUserId={}", coupon.getUserId(), userId);
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }

        // 쿠폰 사용 가능 여부 검증
        if (coupon.getIsUsed().equals("Y")) {
            log.warn("이미 사용된 쿠폰: couponId={}", couponId);
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }

        // 쿠폰 만료 여부 검증
        if (coupon.getExpiredAt() != null && coupon.getExpiredAt().isBefore(LocalDate.now())) {
            log.warn("만료된 쿠폰: couponId={}, expiredAt={}", couponId, coupon.getExpiredAt());
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }

        return coupon;
    }


    /**
     * 퍼센트 할인 금액 계산
     * - 원금 * 할인율 / 100
     * - 소수점 이하 반올림
     * @param originalAmount 원래 금액
     * @param discountPercent 할인율 (%)
     * @return 할인 금액
     */
    public Long calculateCouponDiscount(Long originalAmount, int discountPercent) {

        if (discountPercent <= 0 || discountPercent > 100) {
            log.warn("유효하지 않은 할인율: {}%", discountPercent);
            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
        }

        // 퍼센트 할인 계산 (반올림)
        double discountAmount = originalAmount * discountPercent / 100.0;

        log.info("쿠폰 할인 계산 완료: 할인율={}%, 원래금액={}, 할인액={}",
                discountPercent, originalAmount, discountAmount);

        return Math.round(discountAmount);
    }


//    /**
//     * 쿠폰 검증 및 퍼센트 할인 계산
//     * - PaymentService에서 결제 준비 시 호출
//     * - DISCOUNT 컬럼 값을 퍼센트로 해석 (예: 10 = 10% 할인)
//     *
//     * @param userId 사용자 ID
//     * @param couponId 쿠폰 ID
//     * @param originalAmount 할인 전 원래 금액
//     * @return CouponDiscountResult 할인 계산 결과
//     * @author danjae
//     */
//    @Override
//    public CouponDiscountResult validateAndCalculateDiscount(Long userId, Long couponId, Long originalAmount) {
//        log.info("쿠폰 검증 및 할인 계산 시작: userId={}, couponId={}, amount={}", userId, couponId, originalAmount);
//
//        // 1. 쿠폰 조회 (사용 가능한 쿠폰 목록에서)
//        List<CouponResponse> availableCoupons = couponMapper.getAvailableCouponList(userId);
//        CouponResponse coupon = availableCoupons.stream()
//                .filter(c -> c.getCouponId() == couponId)
//                .findFirst()
//                .orElseThrow(() -> {
//                    log.warn("쿠폰을 찾을 수 없거나 사용할 수 없습니다: userId={}, couponId={}", userId, couponId);
//                    return new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
//                });
//
//        // 2. 쿠폰 소유자 검증
//        if (coupon.getUserId() != userId) {
//            log.warn("쿠폰 소유자 불일치: couponUserId={}, requestUserId={}", coupon.getUserId(), userId);
//            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
//        }
//
//        // 3. 쿠폰 사용 가능 여부 검증
//        if ("Y".equals(coupon.getIsUsed())) {
//            log.warn("이미 사용된 쿠폰: couponId={}", couponId);
//            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
//        }
//
//        if (coupon.getExpiredAt() != null && coupon.getExpiredAt().isBefore(LocalDate.now())) {
//            log.warn("만료된 쿠폰: couponId={}, expiredAt={}", couponId, coupon.getExpiredAt());
//            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
//        }
//
//        // 4. 퍼센트 할인 계산 (DISCOUNT 컬럼 값 = 할인율 %)
//        int discountPercent = coupon.getDiscount();  // 예: 10 = 10%
//        Long discountAmount = calculatePercentDiscount(originalAmount, discountPercent);
//        Long finalAmount = originalAmount - discountAmount;
//
//        // 최종 금액은 0원 이상이어야 함
//        if (finalAmount < 0) {
//            finalAmount = 0L;
//        }
//
//        log.info("쿠폰 할인 계산 완료: 할인율={}%, 원래금액={}, 할인액={}, 최종금액={}",
//                discountPercent, originalAmount, discountAmount, finalAmount);
//
//        return CouponDiscountResult.builder()
//                .couponId(couponId)
//                .couponName(coupon.getName())
//                .discountPercent(discountPercent)
//                .originalAmount(originalAmount)
//                .discountAmount(discountAmount)
//                .finalAmount(finalAmount)
//                .build();
//    }
//
//    /**
//     * 퍼센트 할인 금액 계산
//     * - 원금 * 할인율 / 100
//     * - 소수점 이하 반올림
//     *
//     * @param originalAmount 원래 금액
//     * @param discountPercent 할인율 (%)
//     * @return 할인 금액
//     */
//    private Long calculatePercentDiscount(Long originalAmount, int discountPercent) {
//        if (discountPercent <= 0 || discountPercent > 100) {
//            log.warn("유효하지 않은 할인율: {}%", discountPercent);
//            throw new NotFoundException(ErrorCode.COUPON_NOT_FOUND);
//        }
//
//        // 퍼센트 할인 계산 (반올림)
//        double discountAmount = originalAmount * discountPercent / 100.0;
//        return Math.round(discountAmount);
//    }

    
    // CouponEventListener에서 쿠폰 사용처리함
//    /**
//     * 쿠폰 사용 처리
//     * - PaymentService에서 결제 승인 성공 시 호출
//     * - is_used = 'Y', used_at = 현재 시간
//     *
//     * @param couponId 쿠폰 ID
//     * @author danjae
//     */
//    @Override
//    @Transactional
//    public void applyCouponUsage(Long couponId) {
//        log.info("쿠폰 사용 처리 시작: couponId={}", couponId);
//
//        int updated = couponMapper.useCoupon(couponId);
//        if (updated == 0) {
//            log.error("쿠폰 사용 처리 실패: couponId={}", couponId);
//            throw new BusinessException(ErrorCode.COUPON_FAILED_USED);
//        }
//
//        log.info("쿠폰 사용 처리 완료: couponId={}", couponId);
//    }

    /**
     * 쿠폰 복구 처리
     * - PaymentCompensationService에서 결제 실패/취소 시 호출
     * - is_used = 'N', used_at = NULL (또는 현재 시간)
     *
     * @param couponId 쿠폰 ID
     * @author danjae
     */
    @Override
    @Transactional
    public void revertCouponUsage(Long couponId) {
        log.info("쿠폰 복구 처리 시작: couponId={}", couponId);

        int updated = couponMapper.unuseCoupon(couponId);
        if (updated == 0) {
            log.warn("쿠폰 복구 처리 실패 (이미 미사용 상태일 수 있음): couponId={}", couponId);
        } else {
            log.info("쿠폰 복구 처리 완료: couponId={}", couponId);
        }
    }
}
