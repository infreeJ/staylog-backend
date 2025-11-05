package com.staylog.staylog.domain.auth.service;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    //로그인
    LoginResponse login(LoginRequest loginRequest,
                        HttpServletRequest request,
                        HttpServletResponse response);

    //로그아웃
    void logout(HttpServletRequest request, HttpServletResponse response);

    // 만료된 RefreshToken 삭제 스케줄러
    public void deleteExpiredTokens();

    //AccessToken 갱신
    TokenResponse refreshAccessToken(HttpServletRequest request, HttpServletResponse response);


    // 회원가입
    public long signupUser(SignupRequest signupRequest);


    public NicknameCheckedResponse nicknameDuplicateCheck(String nickname);

    public LoginIdCheckedResponse loginIdDuplicateCheck(String loginId);

//    public EmailCheckedResponse emailDuplicateCheck(String email);
}
