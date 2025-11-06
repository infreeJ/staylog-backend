package com.staylog.staylog.global.exception.custom.refund;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 환불 처리 실패 예외
 */
public class RefundFailedException extends RefundException {
    public RefundFailedException(String message) {
        super(ErrorCode.REFUND_FAILED, message);
    }
}
