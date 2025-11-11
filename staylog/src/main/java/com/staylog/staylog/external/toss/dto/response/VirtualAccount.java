package com.staylog.staylog.external.toss.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Toss Payments 가상계좌 정보
 */
@Getter
@Setter
public class VirtualAccount {

    @JsonProperty("accountType")
    private String accountType;           // 계좌 타입 (일반)

    @JsonProperty("accountNumber")
    private String accountNumber;         // 계좌번호

    @JsonProperty("bankCode")
    private String bankCode;              // 은행 코드

    @JsonProperty("bank")
    private String bank;                  // 은행명 (예: "신한은행")

    @JsonProperty("customerName")
    private String customerName;          // 예금주 (입금자명)

    @JsonProperty("dueDate")
    private OffsetDateTime dueDate;       // 입금 기한

    @JsonProperty("refundStatus")
    private String refundStatus;          // 환불 처리 상태

    @JsonProperty("expired")
    private Boolean expired;              // 만료 여부

    @JsonProperty("settlementStatus")
    private String settlementStatus;      // 정산 상태
}
