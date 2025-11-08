package com.staylog.staylog.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 준비 요청 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreparePaymentRequest {

    @NotNull(message = "예약 ID는 필수입니다")
    private Long bookingId;

    @NotBlank(message = "결제 수단은 필수입니다")
    private String method;  // "CARD", "VIRTUAL_ACCOUNT", "TRANSFER", etc.

    @NotNull(message = "결제 금액은 필수입니다")
    @Positive(message = "결제 금액은 양수여야 합니다")
    private Long amount;  // 할인 전 원래 금액

    /**
     * 쿠폰 ID (선택 사항)
     * - NULL: 쿠폰 미사용
     * - 값 존재: 해당 쿠폰 적용
     */
    private Long couponId;
}
