package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.auth.dto.response.TokenResponse;
import com.staylog.staylog.domain.auth.service.AuthService;
import com.staylog.staylog.global.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "AuthController", description = "인증/인가 API")
@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "사용자 로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response){

        log.info("로그인 요청 : {}", loginRequest.getLoginId());

        LoginResponse loginResponse = authService.login(loginRequest, request, response);
        SuccessResponse<LoginResponse> success = SuccessResponse.of("로그인 성공",loginResponse);
        return ResponseEntity.ok(success);

    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        authService.logout(request, response);
        return ResponseEntity.ok(SuccessResponse.of("로그아웃 성공", null));
    }

    @Operation(summary = "토큰 재발급", description = "RefreshToken을 이용해서 만료된 AccessToken을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<TokenResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.refreshAccessToken(request,response);
        return ResponseEntity.ok(SuccessResponse.of("토큰 재발급 성공", tokenResponse));

    }


    @Operation(summary = "사용자 회원가입", description = "인증이 완료된 이메일을 이용해서 회원가입합니다. \n * userId는 제외하고 데이터를 기입해야합니다.")
    @PostMapping("/user")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> signup(@RequestBody SignupRequest signupRequest) {
        long userId = authService.signupUser(signupRequest);
//        return ResponseEntity.ok(Map.of(
//                "message", "회원가입이 성공적으로 완료되었습니다.",
//                "userId", userId));
        return ResponseEntity.ok(SuccessResponse.of("회원가입이 성공적으로 완료되었습니다.",
                Map.of("userId", userId)));
    }
}

