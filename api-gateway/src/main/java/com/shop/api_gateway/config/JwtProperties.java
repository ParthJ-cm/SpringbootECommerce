package com.shop.api_gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private List<String> publicEndpoints = new ArrayList<>();
    private String secret;
    private long expirationMs;
}
