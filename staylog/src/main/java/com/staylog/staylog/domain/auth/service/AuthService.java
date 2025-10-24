package com.staylog.staylog.domain.auth.service;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.LoginResponse;
import com.staylog.staylog.domain.auth.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    //로그인
    LoginResponse login(LoginRequest loginRequest,
                        HttpServletRequest request,
                        HttpServletResponse response);

    //로그아웃
    void logout(HttpServletRequest request, HttpServletResponse response);

    //AccessToken 갱신
    TokenResponse refreshAccessToken(HttpServletRequest request, HttpServletResponse response);


    // 회원가입
    public long signupUser(SignupRequest signupRequest);
}
