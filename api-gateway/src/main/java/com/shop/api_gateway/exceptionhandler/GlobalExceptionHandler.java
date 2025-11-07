package com.shop.api_gateway.exceptionhandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Map;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;
    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception caught in GlobalExceptionHandler: {}", ex.getMessage(), ex);

        HttpStatus status = determineStatus(ex);

        ApiError apiError = ApiError.builder()
                .statusCode(status.value())
                .error(status.getReasonPhrase())
                .message(resolveMessage(ex))
                .details(Map.of("path", exchange.getRequest().getPath().toString()))
                .build();

        byte[] bytes = null;
        try {
            bytes = objectMapper.writeValueAsBytes(apiError);
        } catch (JsonProcessingException e) {
            bytes = ("{\"statusCode\":500,\"error\":\"Internal Server Error\",\"message\":\"Failed to serialize ApiError.\"}")
                    .getBytes(StandardCharsets.UTF_8);
        }
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    private HttpStatus determineStatus(Throwable ex) {
        return switch (ex) {
            case IllegalArgumentException ignored -> HttpStatus.BAD_REQUEST;
            case ExpiredJwtException ignored -> HttpStatus.UNAUTHORIZED;
            case SignatureException ignored -> HttpStatus.UNAUTHORIZED;
            case ResponseStatusException rse ->
                (rse.getStatusCode() instanceof HttpStatus httpStatus)
                        ? httpStatus
                        : HttpStatus.valueOf(rse.getStatusCode().value());
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private String resolveMessage(Throwable ex) {
        if (ex instanceof ResponseStatusException rse) {
            return rse.getReason() != null ? rse.getReason() : rse.getMessage();
        }
        return ex.getMessage() != null ? ex.getMessage() : "Unexpected error occurred";
    }
}
