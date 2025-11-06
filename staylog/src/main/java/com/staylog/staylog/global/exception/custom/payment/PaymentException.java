package com.staylog.staylog.global.exception.custom.payment;

import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;

/**
 * 결제 관련 기본 예외 클래스
 */
public class PaymentException extends BusinessException {
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
