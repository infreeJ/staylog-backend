package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.MailCheckRequest;
import com.staylog.staylog.domain.auth.dto.request.MailSendRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.auth.dto.response.TokenResponse;
import com.staylog.staylog.domain.auth.service.AuthService;
import com.staylog.staylog.domain.auth.service.MailService;
import com.staylog.staylog.global.common.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response){

        log.info("로그인 요청 : {}", loginRequest.getLoginId());

        LoginResponse loginResponse = authService.login(loginRequest, request, response);
        SuccessResponse<LoginResponse> success = SuccessResponse.of("로그인 성공",loginResponse);
        return ResponseEntity.ok(success);

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        authService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.refreshAccessToken(request,response);
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/auth/mail-send")
    public ResponseEntity<Map<String, String>> sendVerificationMail(@RequestBody MailSendRequest requestDto) {
        mailService.sendVerificationMail(requestDto.getEmail());
        return ResponseEntity.ok(Map.of("message", "인증 메일이 성공적으로 발송되었습니다."));
    }

    @PostMapping("/auth/mail-check")
    public ResponseEntity<Map<String, String>> checkVerificationMail(@RequestBody MailCheckRequest requestDto) {
        boolean isVerified = mailService.verifyMail(requestDto.getEmail(), requestDto.getCode());
        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "이메일 인증에 성공했습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "인증번호가 유효하지 않습니다."));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignupRequest signupRequest) {
        long userId = authService.signupUser(signupRequest);
        return ResponseEntity.ok(Map.of(
                "message", "회원가입이 성공적으로 완료되었습니다.",
                "userId", userId
        ));
    }
}

