package com.shop.user_service.controller;

import com.shop.user_service.DTO.LoginDto;
import com.shop.user_service.DTO.LoginResponseDto;
import com.shop.user_service.DTO.RegistrationRequest;
import com.shop.user_service.DTO.UserDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.service.JWTService;
import com.shop.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JWTService jwtService;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody RegistrationRequest registrationRequest){
        return new ResponseEntity<>(service.registerUser(registrationRequest), HttpStatus.CREATED);
        
    }

    @PostMapping("/logIn")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){
        LoginResponseDto loginResponseDto = service.logIn(loginDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(String refreshToken){
       LoginResponseDto loginResponseDto = service.refreshToken(refreshToken);
       return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/oauth2/success")
    public void handleOAuth2Success(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException, IOException {
        System.out.println("OAuth2 User: " + oAuth2User);
        if (oAuth2User == null) {
            throw new IllegalStateException("OAuth2User is null. Check security context or OAuth2 configuration.");
        }
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("OAuth2 Attributes: " + attributes);
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        if (email == null || name == null) {
            throw new IllegalArgumentException("Email or name not found in OAuth2 user attributes: " + attributes);
        }
        User user = service.findOrCreateUser(email, name);
        String accessToken = jwtService.createToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        String redirectUrl = "http://localhost:5173/login?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        response.sendRedirect(redirectUrl);
    }
}
