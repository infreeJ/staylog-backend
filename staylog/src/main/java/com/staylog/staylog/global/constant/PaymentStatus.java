package com.staylog.staylog.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 결제 상태 Enum
 * COMMON_CODE 테이블의 PAYMENT_STATUS와 매칭
 */
@Getter
@AllArgsConstructor
public enum PaymentStatus {

    PAY_READY("PAY_READY", "결제 준비"),
    PAY_PAID("PAY_PAID", "결제 완료"),
    PAY_WAITING_DEPOSIT("PAY_WAITING_DEPOSIT", "무통장 입금 대기"),
    PAY_FAILED("PAY_FAILED", "결제 실패"),
    PAY_EXPIRED("PAY_EXPIRED", "결제 만료"),
    PAY_REFUND("PAY_REFUND", "환불 완료");

    private final String code;
    private final String description;

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown PaymentStatus code: " + code);
    }
}
