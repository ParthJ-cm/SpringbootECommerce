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
    private final ObjectMapper objectMapper; // Inject ObjectMapper

    @Override
    @Transactional  // <-- Add this
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String email = defaultOAuth2User.getAttribute("email");

        // Get user from DB
        Optional<User> optionalUser = userRepo.findByEmail(email);

        String provider = token.getAuthorizedClientRegistrationId(); // e.g., "google", "facebook"
        String providerId = defaultOAuth2User.getAttribute("sub"); // for Google; for Facebook it might be "id"


        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = User.builder()
                    .name(defaultOAuth2User.getAttribute("name"))
                    .email(email)
                    .password(UUID.randomUUID().toString()) // dummy password for DB
                    .role("USER")
                    .provider(provider)       // set provider
                    .providerId(providerId)   // set providerId
                    .providerType(AuthProviderType.GOOGLE) // or determine dynamically
                    .build();

            user = userRepo.save(user); // save returns User
        }
        System.out.println("User=="+user);
        // Generate JWT with User object
        String accessToken = jwtService.createToken(user);
        String refreshToken = jwtService.createRefreshToken(user);

        // Create LoginResponseDto
        LoginResponseDto loginResponseDto = new LoginResponseDto(user.getId(), accessToken, refreshToken);

        // Build redirect URL with tokens
        String frontendUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/oauth2/callback")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    System.out.println("frontendUrl"+frontendUrl);
        response.sendRedirect(frontendUrl);
    }

}