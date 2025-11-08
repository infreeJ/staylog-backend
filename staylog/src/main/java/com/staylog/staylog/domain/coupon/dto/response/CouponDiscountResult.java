package com.staylog.staylog.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 할인 계산 결과 DTO
 * - PaymentService에서 쿠폰 할인 적용 시 사용
 *
 * @author danjae
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDiscountResult {

    /**
     * 사용한 쿠폰 ID
     */
    private Long couponId;

    /**
     * 쿠폰 이름
     */
    private String couponName;

    /**
     * 할인율 (예: 10% = 10)
     */
    private Integer discountPercent;

    /**
     * 할인 전 원래 금액
     */
    private Long originalAmount;

    /**
     * 쿠폰 할인 금액 (계산된 값)
     */
    private Long discountAmount;

    /**
     * 할인 후 최종 결제 금액
     */
    private Long finalAmount;
}
