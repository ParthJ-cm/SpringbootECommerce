package com.shop.api_gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.api_gateway.config.JwtProperties;
import com.shop.api_gateway.exceptionhandler.ApiError;
import com.shop.api_gateway.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    public JwtAuthenticationFilter(JwtService jwtService, JwtProperties jwtProperties, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (jwtProperties.getPublicEndpoints().stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = jwtService.getClaims(token);
            Long userId = jwtService.validateUserIdFromClaims(claims);
            String email = claims.get("email", String.class);
            String roles = claims.get("roles", String.class);
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId.toString())
                    .header("X-User-Email", email)
                    .header("X-User-Roles", roles)
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }
        catch (Exception e) {
            log.error("Invalid JWT: {}", e.getMessage());
            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ApiError apiError = ApiError.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Invalid or missing JWT token.")
                .build();
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(apiError);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize ApiError", e);
            bytes = "{\"statusCode\":401,\"message\":\"Unauthorized\"}".getBytes(StandardCharsets.UTF_8);
        }
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -1;
    }

}
