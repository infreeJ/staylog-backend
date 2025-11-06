package com.staylog.staylog.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 승인 요청 DTO
 * 프론트엔드에서 Toss SDK를 통해 받은 정보를 전달
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentRequest {

    @NotBlank(message = "paymentKey는 필수입니다")
    private String paymentKey;   // Toss에서 발급한 결제 키

    @NotBlank(message = "orderId는 필수입니다")
    private String orderId;      // BOOKING_NUM (주문번호)

    @NotNull(message = "amount는 필수입니다")
    @Positive(message = "결제 금액은 양수여야 합니다")
    private Long amount;         // 검증용 금액
}
