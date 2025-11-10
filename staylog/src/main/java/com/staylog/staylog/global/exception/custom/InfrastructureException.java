package com.staylog.staylog.global.exception.custom;

import com.staylog.staylog.global.common.code.ErrorCode;
import lombok.Getter;

/**
 * 일시적인 시스템/인프라 오류를 나타내는 예외 클래스(재시도 가능)
 * @author 이준혁
 */
@Getter
public class InfrastructureException extends RuntimeException { // BusinessException과 형제 클래스

    private final ErrorCode errorCode;

    public InfrastructureException(ErrorCode errorCode) {
        super(errorCode.getMessageKey()); // 혹은 errorCode.toString() 등 정책에 맞게
        this.errorCode = errorCode;
    }

    public InfrastructureException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public InfrastructureException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessageKey(), cause);
        this.errorCode = errorCode;
    }

    public InfrastructureException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}