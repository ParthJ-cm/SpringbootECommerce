package com.shop.user_service.DTO;

import lombok.*;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
    private String address;
    private String phoneNo;
}