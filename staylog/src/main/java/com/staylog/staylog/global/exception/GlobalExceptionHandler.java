package com.staylog.staylog.global.exception;

import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.common.response.ErrorResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
import com.staylog.staylog.global.exception.custom.DuplicateLoginIdException;
import com.staylog.staylog.global.exception.custom.DuplicateNicknameException;
import com.staylog.staylog.global.exception.custom.DuplicateEmailException;
import com.staylog.staylog.global.exception.custom.UnverifiedEmailException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
                .errorCode(errorCode.getCode())
                .message(message)
                .status(errorCode.getStatus().value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 회원가입 시 아이디 중복 예외 처리 메서드
     * @auther 이준혁
     * modify 임채호
     */
    @ExceptionHandler(DuplicateLoginIdException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateLoginIdException(DuplicateLoginIdException de,
                                                                         HttpServletRequest request) {
        String message = messageUtil.getMessage(ErrorCode.DUPLICATE_LOGINID.getMessageKey());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.DUPLICATE_LOGINID.getCode())
                .message(message)
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException de,
                                                                         HttpServletRequest request) {
        String message = messageUtil.getMessage(ErrorCode.DUPLICATE_EMAIL.getMessageKey());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.DUPLICATE_EMAIL.getCode())
                .message(message)
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNicknameException(DuplicateNicknameException de,
                                                                         HttpServletRequest request) {
        String message = messageUtil.getMessage(ErrorCode.DUPLICATE_NICKNAME.getMessageKey());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.DUPLICATE_NICKNAME.getCode())
                .message(message)
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnverifiedEmailException.class)
    public ResponseEntity<ErrorResponse> handleUnverifiedEmailException(UnverifiedEmailException ue,
                                                                          HttpServletRequest request) {
        String message = messageUtil.getMessage(ErrorCode.EMAIL_NOT_VERIFIED.getMessageKey());

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(ErrorCode.EMAIL_NOT_VERIFIED.getCode())
                .message(message)
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("로그인 실패: {}", ex.getMessage());

        String message = messageUtil.getMessage(ErrorCode.INVALID_CREDENTIALS.getMessageKey());

        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_CREDENTIALS.getCode())
                .message(message)
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledAccount(DisabledException ex, HttpServletRequest request) {
        log.warn("비활성 계정 접근: {}", ex.getMessage());

        String message = messageUtil.getMessage(ErrorCode.ACCOUNT_DISABLED.getMessageKey());

        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ErrorCode.ACCOUNT_DISABLED.getCode())
                .message(message)
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, HttpServletRequest request) {
        log.warn("JWT 예외 발생: {}", ex.getMessage());

        String message = messageUtil.getMessage(ErrorCode.INVALID_TOKEN.getMessageKey());

        ErrorResponse error = ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_TOKEN.getCode())
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
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(message)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
