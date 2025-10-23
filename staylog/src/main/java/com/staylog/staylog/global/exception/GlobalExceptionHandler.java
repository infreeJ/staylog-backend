package com.staylog.staylog.global.exception;

import com.staylog.staylog.global.exception.custom.DuplicateSignupException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 회원가입 시 아이디 중복 예외 처리 메서드
     * @auther 이준혁
     */
    @ExceptionHandler(DuplicateSignupException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateLoginIdException(DuplicateSignupException de) {
        ErrorResponse response = new ErrorResponse(HttpStatus.CONFLICT.value(), de.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    /**
     * 예외 처리를 위한 Dto
     * @author 이준혁
     */
    @Getter
    public static class ErrorResponse { // 예외 처리를 위한 Dto
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
