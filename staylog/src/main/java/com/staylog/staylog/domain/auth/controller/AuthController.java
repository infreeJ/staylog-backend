package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.auth.dto.response.TokenResponse;
import com.staylog.staylog.domain.auth.service.AuthService;
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

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                               HttpServletRequest request,
                                               HttpServletResponse response){

        log.info("로그인 요청 : {}", loginRequest.getLoginId());

        LoginResponse loginResponse = authService.login(loginRequest, request, response);

        return ResponseEntity.ok(loginResponse);

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


    /**
     * 회원가입 메서드
     * @author 이준혁
     * @return 생성된 유저의 PK
     */    @PostMapping("/user")
    public long UserSignup(SignupRequest signupRequest) {

        return authService.signupUser(signupRequest);
    }
}
}
