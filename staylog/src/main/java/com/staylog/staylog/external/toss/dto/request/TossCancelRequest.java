package com.staylog.staylog.external.toss.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TossCancelRequest {
    private String cancelReason;        // 취소 사유
    private Long cancelAmount;          // 취소 금액 (부분 취소 가능)

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RefundReceiveAccount refundReceiveAccount;  // 가상계좌 환불용 (사용 안 함)
}
