package com.staylog.staylog.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 환불 상태 Enum
 * COMMON_CODE 테이블의 REFUND_STATUS와 매칭
 */
@Getter
@AllArgsConstructor
public enum RefundStatus {

    REQUESTED("REFUND_REQUESTED", "환불 요청"),
    PENDING("REFUND_PENDING", "처리 대기"),
    APPROVED("REFUND_APPROVED", "승인됨"),
    COMPLETED("REFUND_COMPLETED", "완료됨"),
    REJECTED("REFUND_REJECTED", "거부됨"),
    FAILED("REFUND_FAILED", "실패");

    private final String code;
    private final String description;

    public static RefundStatus fromCode(String code) {
        for (RefundStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown RefundStatus code: " + code);
    }
}
