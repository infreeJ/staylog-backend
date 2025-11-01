package com.staylog.staylog.domain.auth.service.impl;

import com.staylog.staylog.domain.auth.dto.EmailVerificationDto;
import com.staylog.staylog.domain.auth.dto.request.LoginRequest;
import com.staylog.staylog.domain.auth.dto.request.SignupRequest;
import com.staylog.staylog.domain.auth.dto.response.*;
import com.staylog.staylog.domain.auth.mapper.AuthMapper;
import com.staylog.staylog.domain.auth.mapper.EmailMapper;
import com.staylog.staylog.domain.auth.service.AuthService;
import com.staylog.staylog.domain.user.dto.UserDto;
import com.staylog.staylog.domain.user.mapper.UserMapper;
import com.staylog.staylog.global.common.code.ErrorCode;
import com.staylog.staylog.global.exception.BusinessException;
import com.staylog.staylog.global.security.entity.RefreshToken;
import com.staylog.staylog.global.security.jwt.JwtTokenProvider;
import com.staylog.staylog.global.security.mapper.RefreshTokenMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final EmailMapper emailMapper;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Override
    public LoginResponse login(LoginRequest loginRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        log.info("로그인 시도: loginId={}", loginRequest.getLoginId());

        // DB에서 사용자 조회 후 인증하기
        UserDto user = authenticate(loginRequest.getLoginId(), loginRequest.getPassword());

        // AccessToken 발급 받기
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole(),
                user.getNickname(),
                user.getLoginId()
        );

        // RefreshToken 발급받기
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        // 기존 활성 토큰 모두 비활성화 (한 계정당 하나의 세션만 유지)
        refreshTokenMapper.deleteByUserId(user.getUserId());
        log.info("기존 활성 토큰 비활성화 완료: userId={}", user.getUserId());

        // RefreshToken DB 저장하기
        refreshTokenMapper.save(refreshToken);

        // RefreshToken을 HttpOnly 쿠키에 저장하기
        jwtTokenProvider.addRefreshTokenToCookie(response, refreshToken.getToken());

        // 유저 마지막 로그인 시간 업데이트
        LocalDateTime currentTime = LocalDateTime.now();
        authMapper.updateLastLogin(user.getUserId(), currentTime);

        user.setLastLogin(currentTime);

        // 로그인 응답
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidity)
                .user(LoginResponse.UserInfo.builder()
                        .userId(user.getUserId())
                        .loginId(user.getLoginId())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .profileImage(user.getProfileImage())
                        .role(user.getRole())
                        .status(user.getStatus())
                        .lastLogin(user.getLastLogin())
                        .build())
                .build();
        log.info("AccesToken : " + loginResponse.getTokenType() + loginResponse.getAccessToken() );
        log.info("expires : "+ loginResponse.getExpiresIn());
        log.info("유저 정보 : " + loginResponse.getUser());
        log.info("로그인 성공: userId={}, loginId={}", user.getUserId(), user.getLoginId());

        return loginResponse;
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("로그아웃 시도");

        // 쿠키에서 RefreshToken 추출하기
        String refreshTokenString = jwtTokenProvider.getRefreshTokenFromCookie(request);

        // refreshToken이 존재한다면
        if (refreshTokenString != null) {
            // DB에서 RefreshToken 무효화 (로그 남기기를 위해 논리적 삭제)
            refreshTokenMapper.deleteByToken(refreshTokenString);
            log.info("RefreshToken 무효화 완료: token={}", refreshTokenString.substring(0, 20) + "...");
        }

        // RefreshToken 쿠키에서도 삭제
        jwtTokenProvider.deleteRefreshTokenCookie(response);

        log.info("로그아웃 성공");
    }

    // 사용자 조회 후 계정 상태 체크
    private UserDto authenticate(String loginId, String password) {

        // DB에서 사용자 조회
        UserDto user = userMapper.findByLoginId(loginId);

        if (user == null) {
            log.warn("로그인 실패: 사용자를 찾을 수 없음 - loginId={}", loginId);
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 계정 상태 확인 (활성/비활성/탈퇴)
        validateUserStatus(user);

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("로그인 실패: 비밀번호 불일치 - loginId={}", loginId);
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        return user;
    }


    /**
     * 만료된 RefreshToken 삭제 스케줄러
     */
    @Override
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    public void deleteExpiredTokens() {
        refreshTokenMapper.deleteExpiredTokens();
    }


    /**
     * AccessToken 갱신
     * RefreshToken을 사용하여 새로운 AccessToken 발급
     */
    @Override
    @Transactional
    public TokenResponse refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        log.info("AccessToken 갱신 시도");

        // 쿠키에서 RefreshToken 추출하고
        String refreshTokenString = jwtTokenProvider.getRefreshTokenFromCookie(request);

        // RefreshToken이 없으면
        if (refreshTokenString == null) {
            log.warn("AccessToken 갱신 실패: RefreshToken이 없습니다.");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // RefreshToken 유효성 검증 (인증서 확인하기)
        if (!jwtTokenProvider.validateToken(refreshTokenString)) {
            log.warn("AccessToken 갱신 실패: 유효하지 않은 RefreshToken");
            throw new JwtException("유효하지 않은 RefreshToken입니다.");
        }

        // RefreshToken 만료 여부 확인
        if (jwtTokenProvider.isTokenExpired(refreshTokenString)) {
            log.warn("AccessToken 갱신 실패: RefreshToken 만료");
            throw new JwtException("RefreshToken이 만료되었습니다.");
        }

        // DB에서 RefreshToken 조회 및 검증
        Optional<RefreshToken> refreshTokenOpt = refreshTokenMapper.findByToken(refreshTokenString);

        if (refreshTokenOpt.isEmpty()) {
            log.warn("AccessToken 갱신 실패: DB에 RefreshToken이 없습니다.");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        RefreshToken refreshToken = refreshTokenOpt.get();

        // RefreshToken 만료 여부 재확인 DB 기준
        if (refreshToken.isExpired()) {
            log.warn("AccessToken 갱신 실패: RefreshToken 만료 DB 기준 - userId={}", refreshToken.getUserId());
            refreshTokenMapper.deleteByToken(refreshTokenString); // 만료된 토큰 무효화
            throw new JwtException("RefreshToken이 만료되었습니다.");
        }

        // 사용자 정보 조회
        Long userId = refreshToken.getUserId();
        Optional<UserDto> userOpt = userMapper.findByUserId(userId);

        if (userOpt.isEmpty()) {
            log.warn("AccessToken 갱신 실패: 사용자를 찾을 수 없음 - userId={}", userId);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        UserDto user = userOpt.get();

        // 계정 상태 확인
        validateUserStatus(user);

        //새로운 AccessToken 발급
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole(),
                user.getNickname(),
                user.getLoginId()
        );

        // 응답 생성
        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidity)
                .build();

        log.info("AccessToken 갱신 성공: userId={}", userId);

        return tokenResponse;
    }


    /**
     * 회원가입 비즈니스 로직
     * @author 이준혁
     * @param signupRequest 입력폼 데이터 Dto
     * @return 생성된 유저의 PK
     */
    @Override
    @Transactional
    public long signupUser(SignupRequest signupRequest) {

        // 이메일 중복 확인
        if (userMapper.findByEmail(signupRequest.getEmail()) != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 아이디 중복 확인
        if (userMapper.findByLoginId(signupRequest.getLoginId()) != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_LOGINID);
        }

        // 닉네임 중복 확인
        if(userMapper.findByNickname(signupRequest.getNickname()) != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        // 이메일 인증 여부 확인
        EmailVerificationDto verification = emailMapper.findVerificationByEmail(signupRequest.getEmail());
        if (verification == null || !"Y".equals(verification.getIsVerified())) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 비밀번호 암호화 및 유저 생성
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        signupRequest.setPassword(encodedPassword);
        authMapper.createUser(signupRequest);

        // 회원가입 완료 후 email_verification 테이블에서 사용된 인증 정보 삭제
        emailMapper.deleteVerificationByEmail(signupRequest.getEmail());

        return signupRequest.getUserId(); // createUser에서 PK를 받아온다
    }


    // 사용자 계정 상태 확인하기
    private void validateUserStatus(UserDto user) {
        if ("INACTIVE".equals(user.getStatus())) {
            log.warn("로그인 실패: 비활성화된 계정 - userId={}", user.getUserId());
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        if ("WITHDRAWN".equals(user.getStatus())) {
            log.warn("로그인 실패: 탈퇴한 계정 - userId={}", user.getUserId());
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
    }



    /**
     * 닉네임 중복 검사 메서드(회원가입 용도)
     * 중복 시 409 응답
     * @author 이준혁
     * @param nickname 유저 닉네임
     * @return nickname, 중복 여부 boolean
     */
    @Override
    public NicknameCheckedResponse nicknameDuplicateCheck(String nickname) {
        UserDto userDto = userMapper.findByNickname(nickname);
        boolean isDuplicate = (userDto != null);
        if(isDuplicate) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return new NicknameCheckedResponse(nickname, isDuplicate);
    }


    /**
     * 아이디 중복 검사 메서드(회원가입 용도)
     * 중복 시 409 응답
     * @author 이준혁
     * @param loginId 유저 아이디
     * @return loginId, 중복 여부 boolean
     */
    public LoginIdCheckedResponse loginIdDuplicateCheck(String loginId) {
        UserDto userDto = userMapper.findByLoginId(loginId);
        boolean isDuplicate = (userDto != null);
        if(isDuplicate) {
            throw new BusinessException(ErrorCode.DUPLICATE_LOGINID);
        }
        return new LoginIdCheckedResponse(loginId, isDuplicate);
    }


}
