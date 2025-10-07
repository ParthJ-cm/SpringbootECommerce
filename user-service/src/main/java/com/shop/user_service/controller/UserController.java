package com.shop.user_service.controller;

import com.shop.user_service.DTO.RegistrationRequest;
import com.shop.user_service.DTO.UserDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")

public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody RegistrationRequest registrationRequest){
        return new ResponseEntity<>(service.registerUser(registrationRequest), HttpStatus.CREATED);
        
    }

//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequest request) {
//        return service.login(request.getEmail(), request.getPassword());
//    }
}
