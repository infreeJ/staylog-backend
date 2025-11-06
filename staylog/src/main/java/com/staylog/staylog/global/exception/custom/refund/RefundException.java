package com.staylog.staylog.global.exception.custom.refund;

import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

/**
 * 환불 관련 기본 예외 클래스
 */
public class RefundException extends BusinessException {
    public RefundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RefundException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
