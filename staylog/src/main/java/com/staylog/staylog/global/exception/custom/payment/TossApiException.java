package com.staylog.staylog.global.exception.custom.payment;

import com.staylog.staylog.global.common.code.ErrorCode;
import lombok.Getter;

/**
 * 토스 페이먼츠 API 호출 실패 예외
 */
@Getter
public class TossApiException extends PaymentException {
    private final String tossErrorCode;
    private final String tossErrorMessage;

    public TossApiException(String tossErrorCode, String tossErrorMessage) {
        super(ErrorCode.TOSS_API_ERROR,
              String.format("Toss API Error [%s]: %s", tossErrorCode, tossErrorMessage));
        this.tossErrorCode = tossErrorCode;
        this.tossErrorMessage = tossErrorMessage;
    }
}
