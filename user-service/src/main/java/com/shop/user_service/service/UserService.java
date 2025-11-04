package com.shop.user_service.service;

import com.shop.user_service.DTO.LoginDto;
import com.shop.user_service.DTO.LoginResponseDto;
import com.shop.user_service.DTO.RegistrationRequest;
import com.shop.user_service.DTO.UserDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.error.InvalidToken;
import com.shop.user_service.error.UserNotFoundException;
import com.shop.user_service.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailService emailService;
    private PasswordEncoder encoder;

    public UserDto registerUser(RegistrationRequest registrationRequest) {
        Optional<User> user = repository
                .findByEmail(registrationRequest.getEmail());
        if (user.isPresent())
            throw new BadCredentialsException("Cannot signup, User already exists with email" + registrationRequest.getEmail());
        User mappedUser = modelMapper.map(registrationRequest, User.class);
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        mappedUser.setConfirmPassword(passwordEncoder.encode(mappedUser.getConfirmPassword()));
        User savedUser = repository.save(mappedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.createToken(user);
        String refreshToken = jwtService.createRefreshToken(user);
        return new LoginResponseDto(user.getUserId(), accessToken, refreshToken);
    }

    public User getUserById(Long userId) {
        return repository.findById(userId).orElseThrow();
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtService.generateUserIdFromToken(refreshToken);
        User user = repository.getUserByUserId(userId);
        String accessToken = jwtService.createToken(user);
        return new LoginResponseDto(user.getUserId(), accessToken, refreshToken);
    }

    public void forgotPassword(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        String token = jwtService.generateResetToken(user.getEmail(), user.getPasswordChangedAt());
        String resetLink = "http://localhost:5173/reset-password?token="+token;
        emailService.sendEmail(user.getEmail(), "Password Reset Link", resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        Claims claims = jwtService.getResetTokenClaims(token);
        String email = claims.getSubject();
        String tokenPasswordChangedAt = claims.get("passwordChangedAt", String.class);
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if (user.getPasswordChangedAt().isAfter(LocalDateTime.parse(tokenPasswordChangedAt))) {
            throw new InvalidToken("Token is invalid or expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now()); // update timestamp
        repository.save(user);
    }

    public void validateResetToken(String token) {
        Claims claims = jwtService.getResetTokenClaims(token);
        String email = claims.getSubject();
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new InvalidToken("Token is invalid or expired"));
        if (claims.getExpiration().before(new Date())) {
            throw new InvalidToken("Token is invalid or expired");
        }
        String tokenPasswordChangedAt = claims.get("passwordChangedAt", String.class);
        if (user.getPasswordChangedAt().isAfter(LocalDateTime.parse(tokenPasswordChangedAt))) {
            throw new InvalidToken("Token is invalid or expired");
        }
    }
}
