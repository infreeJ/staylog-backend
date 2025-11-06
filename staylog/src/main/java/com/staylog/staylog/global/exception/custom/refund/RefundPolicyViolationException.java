package com.staylog.staylog.global.exception.custom.refund;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 환불 정책 위반 예외
 */
public class RefundPolicyViolationException extends RefundException {
    public RefundPolicyViolationException(String message) {
        super(ErrorCode.REFUND_POLICY_VIOLATION, message);
    }
}
