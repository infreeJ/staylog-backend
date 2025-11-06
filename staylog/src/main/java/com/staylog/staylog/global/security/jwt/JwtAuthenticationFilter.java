package com.staylog.staylog.global.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 인증 필터
 * - 모든 요청에 대해 JWT 토큰을 검증하고 SecurityContext에 인증 정보를 설정합니다.
 * - Authorization 헤더에서 Bearer 토큰을 추출하여 검증합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Authorization 헤더에서 JWT 토큰 추출
            String token = jwtTokenProvider.getTokenFromHeader(request);

            // 토큰이 존재하고 유효한 경우
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                // 토큰이 만료되지 않았는지 확인
                if (!jwtTokenProvider.isTokenExpired(token)) {

                    // 토큰에서 사용자 정보 추출
                    Long userId = jwtTokenProvider.getUserIdFromToken(token);
                    String email = jwtTokenProvider.getEmailFromToken(token);
                    String role = jwtTokenProvider.getRoleFromToken(token);

                    // JWT Claims에서 추가 정보 추출
                    io.jsonwebtoken.Claims claims = jwtTokenProvider.getClaimsFromToken(token);
                    String nickname = claims.get("nickname", String.class);
                    String loginId = claims.get("loginId", String.class);

                    // SecurityUser 객체 생성
                    com.staylog.staylog.global.security.SecurityUser securityUser =
                        new com.staylog.staylog.global.security.SecurityUser(
                            userId,
                            email,
                            role,
                            nickname,
                            loginId
                        );

                    // Spring Security 인증 객체 생성
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            securityUser, // principal (SecurityUser 객체)
                            null,         // credentials (비밀번호는 불필요)
                            Collections.singletonList(new SimpleGrantedAuthority(role)) // authorities (권한)
                        );

                    // 요청 세부 정보 설정
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext에 인증 정보 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("JWT 인증 성공 - userId: {}, email: {}, role: {}", userId, email, role);
                } else {
                    log.warn("만료된 JWT 토큰입니다.");
                }
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
