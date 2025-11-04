package com.shop.user_service.controller;

import com.shop.user_service.DTO.*;
import com.shop.user_service.service.JWTService;
import com.shop.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final JWTService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody RegistrationRequest registrationRequest){
        return new ResponseEntity<>(service.registerUser(registrationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){
        LoginResponseDto loginResponseDto = service.login(loginDto);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(String refreshToken){
       LoginResponseDto loginResponseDto = service.refreshToken(refreshToken);
       return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<StandardResponse<String>> forgotPassword(@RequestBody @Valid ForgotPasswordDto request) {
        service.forgotPassword(request.getEmail());
        StandardResponse<String> response = new StandardResponse<>(200,
        null,
        "Password reset link sent to your email.",
        null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<StandardResponse<String>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        service.resetPassword(request.getToken(), request.getNewPassword());
        StandardResponse<String> response = new StandardResponse<>(200,
                null,
                "Password has been successfully reset.",
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<StandardResponse<String>> validateResetToken(@RequestParam String token) {
        service.validateResetToken(token);
        StandardResponse<String> response = new StandardResponse<>(200,
                null,
                "Reset token is valid",
                null
        );
        return ResponseEntity.ok(response);
    }
}
