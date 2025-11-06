package com.staylog.staylog.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 환불 유형 Enum
 * COMMON_CODE 테이블의 REFUND_TYPE과 매칭
 */
@Getter
@AllArgsConstructor
public enum RefundType {

    FULL("REFUND_FULL", 100, "전액 환불"),
    EIGHTY("REFUND_EIGHTY", 80, "80% 환불"),
    FIFTY("REFUND_FIFTY", 50, "50% 환불"),
    THIRTY("REFUND_THIRTY", 30, "30% 환불"),
    CUSTOM("REFUND_CUSTOM", 0, "커스텀 환불");

    private final String code;
    private final int percentage;
    private final String description;

    public static RefundType fromCode(String code) {
        for (RefundType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RefundType code: " + code);
    }
}
