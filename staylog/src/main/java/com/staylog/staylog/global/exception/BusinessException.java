package com.staylog.staylog.global.exception;

import com.staylog.staylog.global.common.code.ErrorCode;
import lombok.Getter;

/**
 * 비즈니스 로직 예외 기본 클래스
 * ErrorCode를 사용하여 일관된 예외 처리
 *
 * @author 임채호
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * ErrorCode로 예외 생성
     *
     * @param errorCode 에러 코드
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 추가 메시지로 예외 생성
     *
     * @param errorCode 에러 코드
     * @param message 추가 메시지
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 원인 예외로 예외 생성
     *
     * @param errorCode 에러 코드
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessageKey(), cause);
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode, 추가 메시지, 원인 예외로 예외 생성
     *
     * @param errorCode 에러 코드
     * @param message 추가 메시지
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
