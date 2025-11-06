package com.staylog.staylog.external.toss.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 토스 카드 결제 정보
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private String company;           // 카드사 (예: 현대카드)
    private String number;            // 카드번호 (마스킹된 번호)
    private Integer installmentPlanMonths; // 할부 개월 수 (0: 일시불)
    private String approveNo;         // 승인번호
    private String cardType;          // 카드 유형 (신용, 체크, 기프트)
    private String ownerType;         // 소유자 유형 (개인, 법인)
    private String acquireStatus;     // 매입 상태
    private Boolean isInterestFree;   // 무이자 할부 여부
    private String interestPayer;     // 무이자 할부 부담 주체
}
