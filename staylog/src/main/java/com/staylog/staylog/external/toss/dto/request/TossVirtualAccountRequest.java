package com.staylog.staylog.external.toss.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Toss Payments 가상계좌 발급 요청 DTO
 * API: POST /v1/virtual-accounts
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TossVirtualAccountRequest {

    @JsonProperty("orderId")
    private String orderId;        // 주문번호 (BOOKING_NUM)

    @JsonProperty("orderName")
    private String orderName;      // 주문명 (숙소명 - 객실명)

    @JsonProperty("amount")
    private Long amount;           // 금액

    @JsonProperty("customerName")
    private String customerName;   // 고객명

    @JsonProperty("bank")
    private String bank;           // 은행 코드 (선택, 없으면 랜덤 발급)

    @JsonProperty("validHours")
    private Integer validHours;    // 유효 시간 (기본 24시간)

    @JsonProperty("customerMobilePhone")
    private String customerMobilePhone;  // 고객 휴대폰번호 (선택이지만 권장)

    @JsonProperty("taxFreeAmount")
    private Long taxFreeAmount;    // 비과세 금액 (기본 0)
}
