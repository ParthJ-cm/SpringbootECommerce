package com.shop.user_service.DTO;

import com.shop.user_service.type.AuthProviderType;
import lombok.*;

import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
    private String address;
    private String phoneNo;
    private AuthProviderType provider;  // Add this
    private String providerId;
}