package com.shop.user_service.security;

import com.shop.user_service.entity.User;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.service.JWTService;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j // For logging
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = requestTokenHeader.substring(7); // Remove "Bearer " prefix
        try {
            Long userId = jwtService.generateUserIdFromToken(token);
            if (userId == null) {
                log.warn("Unable to extract userId from token: {}", token);
                filterChain.doFilter(request, response);
                return;
            }
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                log.warn("User not found for userId: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }
            // Set authorities based on user's role
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, Collections.singletonList(authority));

            // Set authentication details
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set authentication in Security Context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("Authenticated user: {} with role: {}", user.getEmail(), user.getRole());

        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}