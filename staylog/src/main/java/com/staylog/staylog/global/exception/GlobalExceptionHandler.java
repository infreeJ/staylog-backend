package com.staylog.staylog.global.exception;

import com.staylog.staylog.global.common.response.ErrorResponse;
import com.staylog.staylog.global.exception.custom.DuplicateSignupException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 회원가입 시 아이디 중복 예외 처리 메서드
     * @auther 이준혁
     * modify 임채호
     */
    @ExceptionHandler(DuplicateSignupException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateLoginIdException(DuplicateSignupException de,
                                                                         HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .errorCode("DUPLICATE_USER")
                .message("이미 사용 중인 아이디입니다.") //사용자에게 보여줄 message
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .build();

        //HttpStatus.CONFILCT로 상태코드 직접 지정해주려면 <> 제너릭 비워주기
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        log.warn("로그인 실패: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("INVALID_CREDENTIALS")
                .message("아이디 또는 비밀번호가 올바르지 않습니다.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledAccount(DisabledException ex, HttpServletRequest request) {
        log.warn("비활성 계정 접근: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("ACCOUNT_DISABLED")
                .message("현재 계정은 비활성화 상태입니다. 관리자에게 문의하세요.")
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException ex, HttpServletRequest request) {
        log.warn("JWT 예외 발생: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("INVALID_TOKEN")
                .message("유효하지 않은 인증 토큰입니다. 다시 로그인해주세요.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("서버 오류 발생", ex);
        ErrorResponse error = ErrorResponse.builder()
                .errorCode("INTERNAL_ERROR")
                .message("서버 오류가 발생했습니다.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
