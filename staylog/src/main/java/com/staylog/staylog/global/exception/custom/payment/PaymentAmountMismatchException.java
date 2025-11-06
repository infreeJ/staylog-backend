package com.staylog.staylog.global.exception.custom.payment;

import com.staylog.staylog.global.common.code.ErrorCode;

/**
 * 결제 금액 불일치 예외
 */
public class PaymentAmountMismatchException extends PaymentException {
    public PaymentAmountMismatchException() {
        super(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
    }

    public PaymentAmountMismatchException(Long expected, Long actual) {
        super(ErrorCode.PAYMENT_AMOUNT_MISMATCH,
              String.format("Amount mismatch: expected=%d, actual=%d", expected, actual));
    }
}
