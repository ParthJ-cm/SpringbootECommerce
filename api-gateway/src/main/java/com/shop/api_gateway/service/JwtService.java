package com.shop.api_gateway.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.SignatureException;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(generateSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException ex) {
            log.warn("Invalid JWT signature: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token expired: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT token is empty or null: {}", ex.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long generateUserIdFromToken(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        Claims claims = getClaims(token);
        Object userIdObj = claims.get("userId"); // userId must be included when token is generated

        if (userIdObj == null) {
            throw new IllegalArgumentException("userId not present in token");
        }
        try {
            return Long.parseLong(userIdObj.toString());
        } catch (NumberFormatException ex) {
            log.error("Invalid userId format in token: {}", ex.getMessage());
            throw new IllegalArgumentException("Invalid userId format in token");
        }
    }

}
