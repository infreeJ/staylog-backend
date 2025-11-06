package com.staylog.staylog.external.toss.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 가상계좌 환불용 계좌 정보
 * (현재 프로젝트에서는 사용하지 않음)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundReceiveAccount {
    private String bank;           // 은행 코드
    private String accountNumber;  // 계좌번호
    private String holderName;     // 예금주명
}
