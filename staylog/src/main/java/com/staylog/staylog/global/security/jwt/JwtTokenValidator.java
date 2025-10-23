package com.staylog.staylog.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenValidator(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // AccessToken 유효성 검증 (타입이 ACCESS인지, 만료되지 않았는지 확인)
    public boolean validateAccessToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                return false;
            }

            String tokenType = jwtTokenProvider.getTokenTypeFromToken(token);
            if (!"ACCESS".equals(tokenType)) {
                return false;
            }

            return !jwtTokenProvider.isTokenExpired(token);

        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // RefreshToken 유효성 검증 (타입이 REFRESH인지, 만료되지 않았는지 확인)
    public boolean validateRefreshToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return false;
            }

            if (!jwtTokenProvider.validateToken(token)) {
                return false;
            }

            String tokenType = jwtTokenProvider.getTokenTypeFromToken(token);
            if (!"REFRESH".equals(tokenType)) {
                return false;
            }

            return !jwtTokenProvider.isTokenExpired(token);

        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 검증 후 상세 결과 반환 (유효/만료/무효 구분)
    public TokenValidationResult validateTokenWithDetails(String token) {
        try {
            if (token == null || token.isEmpty()) {
                return TokenValidationResult.invalid("Token is null or empty");
            }

            if (!jwtTokenProvider.validateToken(token)) {
                return TokenValidationResult.invalid("Token signature is invalid");
            }

            if (jwtTokenProvider.isTokenExpired(token)) {
                return TokenValidationResult.expired("Token has expired");
            }

            return TokenValidationResult.valid();

        } catch (ExpiredJwtException e) {
            return TokenValidationResult.expired("Token has expired");
        } catch (SignatureException e) {
            return TokenValidationResult.invalid("Invalid token signature");
        } catch (MalformedJwtException e) {
            return TokenValidationResult.invalid("Malformed token");
        } catch (UnsupportedJwtException e) {
            return TokenValidationResult.invalid("Unsupported token");
        } catch (IllegalArgumentException e) {
            return TokenValidationResult.invalid("Token claims string is empty");
        } catch (Exception e) {
            return TokenValidationResult.invalid("Unknown token validation error: " + e.getMessage());
        }
    }

    public static class TokenValidationResult {
        private final boolean valid;
        private final boolean expired;
        private final String message;

        private TokenValidationResult(boolean valid, boolean expired, String message) {
            this.valid = valid;
            this.expired = expired;
            this.message = message;
        }

        public static TokenValidationResult valid() {
            return new TokenValidationResult(true, false, "Token is valid");
        }

        public static TokenValidationResult invalid(String message) {
            return new TokenValidationResult(false, false, message);
        }

        public static TokenValidationResult expired(String message) {
            return new TokenValidationResult(false, true, message);
        }

        public boolean isValid() {
            return valid;
        }

        public boolean isExpired() {
            return expired;
        }

        public String getMessage() {
            return message;
        }
    }
}
