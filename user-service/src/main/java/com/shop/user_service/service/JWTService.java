package com.shop.user_service.service;

import com.shop.user_service.Entity.User;
import com.shop.user_service.error.InvalidToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration-ms}")
    private long JWT_ACCESS_TOKEN_EXPIRES_IN;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long JWT_REFRESH_TOKEN_EXPIRES_IN;

    @Value("${jwt.reset-token-expiration-ms}")
    private long JWT_RESET_TOKEN_EXPIRES_IN;

    private SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(User user){
        return Jwts.builder()
                .subject(user.getUserId().toString())
                .claim("email",user.getEmail())
                .claim("roles",user.getRole())
                .claim("userId", user.getUserId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_EXPIRES_IN))
                .signWith(generateSecretKey())
                .compact();
    }
    public Long generateUserIdFromToken(String token) {
        Claims claim = Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claim.getSubject());
    }

    public String createRefreshToken(User user){
        return Jwts.builder()
                .subject(user.getUserId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ JWT_REFRESH_TOKEN_EXPIRES_IN)) //here we have to set it for long time 6 months
                .signWith(generateSecretKey())
                .compact();
    }

    public String generateResetToken(String email, LocalDateTime passwordChangedAt) {
        return Jwts.builder()
                .subject(email)
                .claim("passwordChangedAt", passwordChangedAt.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_RESET_TOKEN_EXPIRES_IN)) // 15 min expiry
                .signWith(generateSecretKey())
                .compact();
    }

    public Claims getResetTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (ExpiredJwtException e) {
            throw new InvalidToken("Reset token expired");
        } catch (JwtException e) {
            throw new InvalidToken("Invalid reset token");
        }
    }

}
