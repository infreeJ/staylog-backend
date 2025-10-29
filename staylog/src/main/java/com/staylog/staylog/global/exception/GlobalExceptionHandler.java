package com.staylog.staylog.global.exception;

import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.common.response.ErrorResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageUtil messageUtil;

    /**
     * BusinessException 처리
     * @author 임채호
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        String message = messageUtil.getMessage(errorCode.getMessageKey());

        log.warn("비즈니스 예외 발생 - Code: {}, Message: {}", errorCode.getCode(), message);

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message)
                .status(errorCode.getStatus().value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, HttpServletRequest request) {
        log.warn("JWT 예외 발생: {}", ex.getMessage());

        String message = messageUtil.getMessage(ErrorCode.INVALID_TOKEN.getMessageKey());

        ErrorResponse error = ErrorResponse.builder()
                .code(ErrorCode.INVALID_TOKEN.getCode())
                .message(message)
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("서버 오류 발생", ex);

        String message = messageUtil.getMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessageKey());

        ErrorResponse error = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(message)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
