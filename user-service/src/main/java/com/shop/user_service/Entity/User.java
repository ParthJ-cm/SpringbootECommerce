package com.shop.user_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users") // ✅ avoids PostgreSQL keyword issue
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @JsonIgnore // ✅ hides password in API responses
    @NotBlank(message = "Password is required")
    private String password;

    @Transient // ✅ not stored in DB
    @NotBlank(message = "ConfirmPassword is required")
    private String confirmPassword;

    private String role;
    private String address;
    private String phoneNo;
}
