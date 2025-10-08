package com.shop.user_service.service;

import com.shop.user_service.DTO.LoginDto;
import com.shop.user_service.DTO.LoginResponseDto;
import com.shop.user_service.DTO.RegistrationRequest;
import com.shop.user_service.DTO.UserDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepo;
    private PasswordEncoder encoder;

    public UserDto registerUser(RegistrationRequest registrationRequest) {
        Optional<User> user = repository
                .findByEmail(registrationRequest.getEmail());
        if (user.isPresent())
            throw new BadCredentialsException("Cannot signup, User already exists with email" + registrationRequest.getEmail());

        User mappedUser = modelMapper.map(registrationRequest, User.class);
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        mappedUser.setConfirmPassword(passwordEncoder.encode(mappedUser.getConfirmPassword()));

        //Save the user to database
        User savedUser = repository.save(mappedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public LoginResponseDto logIn(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.createToken(user);
        String refreshToken = jwtService.createRefreshToken(user);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow();

    }

    public LoginResponseDto refreshToken(String refreshToken){
        Long userId = jwtService.generateUserIdFromToken(refreshToken);
        User user = userRepo.getUserById(userId);

        String accessToken = jwtService.createToken(user);
        return  new LoginResponseDto(user.getId(),accessToken,refreshToken);

    }


}
