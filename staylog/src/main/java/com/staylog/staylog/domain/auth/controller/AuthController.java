package com.staylog.staylog.domain.auth.controller;

import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.*;
import com.staylog.staylog.domain.auth.service.AuthService;
import com.staylog.staylog.domain.user.service.UserService;
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
import org.springframework.web.bind.annotation.*;

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
        String code = SuccessCode.LOGIN_SUCCESS.name();
        SuccessResponse<LoginResponse> success = SuccessResponse.of(code,message, loginResponse);
        return ResponseEntity.ok(success);

    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
    @PostMapping("/auth/logout")
    public ResponseEntity<SuccessResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        authService.logout(request, response);
        String message = messageUtil.getMessage(SuccessCode.LOGOUT_SUCCESS.getMessageKey());
        String code = SuccessCode.LOGOUT_SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code,message, null));
    }

    @Operation(summary = "토큰 재발급", description = "RefreshToken을 이용해서 만료된 AccessToken을 재발급합니다.")
    @PostMapping("/auth/refresh")
    public ResponseEntity<SuccessResponse<TokenResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        TokenResponse tokenResponse = authService.refreshAccessToken(request,response);
        String message = messageUtil.getMessage(SuccessCode.TOKEN_REFRESHED.getMessageKey());
        String code = SuccessCode.TOKEN_REFRESHED.name();
        return ResponseEntity.ok(SuccessResponse.of(code,message, tokenResponse));

    }


    /**
     * 사용자 회원가입 컨트롤러 메서드
     * 성공 시 200, 실패 시 400 응답
     * @author 이준혁
     * @param signupRequest 회원가입 입력폼 데이터
     * @return userId 생성된 유저의 PK (마이페이지 이동 목적)
     */
    @Operation(summary = "사용자 회원가입", description = "인증이 완료된 이메일을 이용해서 회원가입합니다. \n * userId는 제외하고 데이터를 기입해야합니다. \n * role은 대문자로 'USER' 입력하세요.")
    @PostMapping("/user")
    public ResponseEntity<SuccessResponse<Map<String, Object>>> signup(@RequestBody SignupRequest signupRequest) {
        long userId = authService.signupUser(signupRequest);
        String message = messageUtil.getMessage(SuccessCode.SIGNUP_SUCCESS.getMessageKey());
        String code = SuccessCode.SIGNUP_SUCCESS.name();
        return ResponseEntity.ok(SuccessResponse.of(code,message, Map.of("userId", userId)));
    }


    /**
     * 닉네임 중복 확인 컨트롤러
     * 성공 시 200, 중복 시 409 응답
     * @author 이준혁
     * @param nickname 닉네임
     * @return NicknameCheckedResponse 닉네임, 중복 여부 boolean
     */
    @Operation(summary = "닉네임 중복 확인", description = "회원가입 시 닉네임 중복 확인 API입니다. \n * 중복 = 409 \n * 사용 가능한 닉네임 = 200")
    @GetMapping("/user/nickname/{nickname}/duplicate")
    public ResponseEntity<SuccessResponse<NicknameCheckedResponse>> nicknameDuplicateCheck(@PathVariable String nickname) {
        NicknameCheckedResponse data = authService.nicknameDuplicateCheck(nickname);
        String message = messageUtil.getMessage(SuccessCode.USER_NICKNAME_CHECKED.getMessageKey());
        String code = SuccessCode.USER_NICKNAME_CHECKED.name();
        return ResponseEntity.ok(SuccessResponse.of(code,message, data));
    }

    /**
     * 아이디 중복 확인 컨트롤러
     * 성공 시 200, 중복 시 409 응답
     * @author 이준혁
     * @param loginId 아이디
     * @return LoginIdCheckedResponse 아이디, 중복 여부 boolean
     */
    @Operation(summary = "아이디 중복 확인", description = "회원가입 시 아이디 중복 확인 API입니다. \n * 중복 = 409 \n * 사용 가능한 아이디 = 200")
    @GetMapping("/user/loginId/{loginId}/duplicate")
    public ResponseEntity<SuccessResponse<LoginIdCheckedResponse>> loginIdDuplicateCheck(@PathVariable String loginId) {
        LoginIdCheckedResponse data = authService.loginIdDuplicateCheck(loginId);
        String message = messageUtil.getMessage(SuccessCode.USER_LOGINID_CHECKED.getMessageKey());
        String code = SuccessCode.USER_LOGINID_CHECKED.name();
        return ResponseEntity.ok(SuccessResponse.of(code,message, data));
    }

}

