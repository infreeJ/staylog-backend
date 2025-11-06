package com.staylog.staylog.external.toss.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 토스 간편결제 정보
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EasyPay {
    private String provider;          // 간편결제사 (토스페이, 페이코, 삼성페이 등)
    private Long amount;              // 간편결제 금액
    private Long discountAmount;      // 간편결제 서비스의 적립 포인트나 쿠폰 등으로 할인된 금액
}
