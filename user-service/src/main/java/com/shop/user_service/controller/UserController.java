package com.shop.user_service.controller;

import com.shop.user_service.DTO.LoginDto;
import com.shop.user_service.DTO.LoginResponseDto;
import com.shop.user_service.DTO.RegistrationRequest;
import com.shop.user_service.DTO.UserDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

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

}
