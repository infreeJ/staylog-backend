package com.staylog.staylog.global.security.jwt;

import com.staylog.staylog.global.security.entity.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final String issuer;
    private final String refreshTokenCookieName;
    private final boolean httpOnly;
    private final boolean secure;
    private final String domain;
    private final String path;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.cookie.refresh-token-name}") String refreshTokenCookieName,
            @Value("${jwt.cookie.http-only}") boolean httpOnly,
            @Value("${jwt.cookie.secure}") boolean secure,
            @Value("${jwt.cookie.domain}") String domain,
            @Value("${jwt.cookie.path}") String path
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret.trim()));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.issuer = issuer;
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.httpOnly = httpOnly;
        this.secure = secure;
        this.domain = domain;
        this.path = path;
    }

    // AccessToken 생성 (userId, email, role 포함, 30분 유효)
    public String generateAccessToken(Long userId, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .claim("type", "ACCESS")
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 생성 (userId만 포함, 7일 유효)
    public RefreshToken generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidity);

        String tokenString = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("type", "REFRESH")
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // RefreshToken 엔티티 객체 생성 및 반환
        LocalDateTime expiresAtLocalDateTime = new Timestamp(expiryDate.getTime()).toLocalDateTime();
        return new RefreshToken(
                userId,
                tokenString,
                expiresAtLocalDateTime
        );
    }

    // 토큰에서 Claims 추출
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("email", String.class);
    }

    // 토큰에서 권한(Role) 추출
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    // 토큰 타입 추출 (ACCESS 또는 REFRESH)
    public String getTokenTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("type", String.class);
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 토큰 유효성 검증 (서명 및 구조 검증)
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // RefreshToken을 HttpOnly 쿠키에 추가
    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(refreshTokenCookieName, refreshToken);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setMaxAge((int) (refreshTokenValidity / 1000));
        response.addCookie(cookie);
    }

    // 쿠키에서 RefreshToken 추출
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, refreshTokenCookieName);
    }

    // 쿠키에서 특정 이름의 토큰 추출
    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    public String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 쿠키에서 RefreshToken 삭제 (로그아웃 시 사용)
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie(refreshTokenCookieName, null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath(path);
        refreshTokenCookie.setDomain(domain);
        response.addCookie(refreshTokenCookie);
    }
}
