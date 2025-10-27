package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.auth.dto.response.TokenResponse;
import com.staylog.staylog.domain.auth.service.AuthService;
import com.staylog.staylog.global.common.code.SuccessCode;
import com.staylog.staylog.global.common.response.SuccessResponse;
import com.staylog.staylog.global.common.util.MessageUtil;
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
@RequestMapping("/v1")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MessageUtil messageUtil;

    /**
     * 로그인/ 로그아웃/ 리프레쉬 컨트롤러
     * @Author 임채호
     *
     * 클라이언트 단에서 XSS 공격을 방지하기 위해 Refresh Token을 HttpOnly Cookie에 저장하였다.
     * HttpOnly Cookie는 브라우저가 자동으로 전송하고, 자바스크립트에서 접근할 수 없기 때문에 보안상 안전하다.
     * 또한 RequestHeader("Authorization")로는 쿠키 정보를 직접 다룰 수 없고,
     * 헤더 기반 방식은 쿠키의 자동 전송 및 제어가 제한적이기 떄문에
     * Refresh Token을 처리하기 위해 HttpServletRequest와 HttpServletResponse를 사용하였다.
     **/

    @Operation(summary = "로그인", description = "사용자 로그인")
    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response){

        log.info("로그인 요청 : {}", loginRequest.getLoginId());

        LoginResponse loginResponse = authService.login(loginRequest, request, response);
        String message = messageUtil.getMessage(SuccessCode.LOGIN_SUCCESS.getMessageKey());
        SuccessResponse<LoginResponse> success = SuccessResponse.of(message, loginResponse);
        return ResponseEntity.ok(success);

    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
    @PostMapping("/auth/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        authService.logout(request, response);
        String message = messageUtil.getMessage(SuccessCode.LOGOUT_SUCCESS.getMessageKey());
        return ResponseEntity.ok(SuccessResponse.of(message, null));
    }

    @Operation(summary = "토큰 재발급", description = "RefreshToken을 이용해서 만료된 AccessToken을 재발급합니다.")
    @PostMapping("/auth/refresh")
    public ResponseEntity<SuccessResponse<TokenResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.refreshAccessToken(request,response);
        String message = messageUtil.getMessage(SuccessCode.TOKEN_REFRESHED.getMessageKey());
        return ResponseEntity.ok(SuccessResponse.of(message, tokenResponse));

    }


    @Operation(summary = "사용자 회원가입", description = "인증이 완료된 이메일을 이용해서 회원가입합니다. \n * userId는 제외하고 데이터를 기입해야합니다.")
    @PostMapping("/user")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> signup(@RequestBody SignupRequest signupRequest) {
        long userId = authService.signupUser(signupRequest);
        String message = messageUtil.getMessage(SuccessCode.SIGNUP_SUCCESS.getMessageKey());
        return ResponseEntity.ok(SuccessResponse.of(message, Map.of("userId", userId)));
    }
}

