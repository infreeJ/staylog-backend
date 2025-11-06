package com.staylog.staylog.domain.refund.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 환불 요청 DTO
 * - 예약 취소 및 환불 요청
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRefundRequest {

    @NotNull(message = "예약 ID는 필수입니다")
    private Long bookingId;

    @NotBlank(message = "환불 사유는 필수입니다")
    @Size(max = 500, message = "환불 사유는 500자 이하여야 합니다")
    private String refundReason;
}
