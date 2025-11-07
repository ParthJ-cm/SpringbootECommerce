package com.shop.product_service.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.product_service.context.UserContext;
import com.shop.product_service.context.UserContextHolder;
import com.shop.product_service.exceptionhandler.ApiError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
    private final UserContextHolder userContextHolder;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String rolesHeader = request.getHeader("X-User-Roles");

        if (userIdHeader != null && email != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (rolesHeader != null) {
                authorities = Arrays.stream(rolesHeader.split(","))
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                        .collect(Collectors.toList());
            }
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserContext userContext = UserContext.builder()
                    .userId(Long.parseLong(userIdHeader))
                    .email(email)
                    .roles(authorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .map(r -> r.replace("ROLE_", ""))
                            .toList())
                    .build();
            userContextHolder.setUserContext(userContext);
            try {
                filterChain.doFilter(request, response);
            } finally {
                userContextHolder.clear();
                SecurityContextHolder.clearContext();
            }
        }
        else {
            HttpStatus status = HttpStatus.UNAUTHORIZED;

            ApiError apiError = ApiError.builder()
                    .statusCode(status.value())
                    .error(status.getReasonPhrase())
                    .message("You are not authorized to access the resource.")
                    .build();

            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), apiError);
        }
    }
}