package com.shop.user_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.user_service.DTO.LoginResponseDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.service.JWTService;
import com.shop.user_service.type.AuthProviderType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepo;
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        CustomOAuth2User customUser = (CustomOAuth2User) token.getPrincipal();
        User user = customUser.getUser();
        String accessToken = jwtService.createToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        String frontendUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/callback")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
        response.sendRedirect(frontendUrl);
    }
}