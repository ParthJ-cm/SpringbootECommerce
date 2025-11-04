package com.shop.user_service.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.user_service.type.AuthProviderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username is required")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    @Column(nullable = false, length = 50, unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String password;

    @Transient
    private String confirmPassword;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Size(max = 100)
    @Column(name = "address_line1", length = 100)
    private String addressLine1;

    @Size(max = 100)
    @Column(name = "address_line2", length = 100)
    private String addressLine2;

    @Size(max = 50)
    @Column(length = 50)
    private String city;

    @Size(max = 50)
    @Column(length = 50)
    private String state;

    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Size(max = 50)
    @Column(length = 50)
    private String country;

    @NotBlank(message = "Role is required")
    @Column(length = 50, nullable = false)
    private String role = "customer";

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", length = 50)
    private AuthProviderType provider;

    @Column(name = "provider_id", length = 255)
    private String providerId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @PrePersist
    public void prePersist() {
        if (passwordChangedAt == null) {
            passwordChangedAt = LocalDateTime.now();
        }
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role != null
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                : Collections.emptyList();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email; // using email as username for login
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return isActive;
    }
}
