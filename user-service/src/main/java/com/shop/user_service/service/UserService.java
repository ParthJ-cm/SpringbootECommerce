package com.shop.user_service.service;

import com.shop.user_service.DTO.RegistrationRequest;
import com.shop.user_service.DTO.UserDto;
import com.shop.user_service.Entity.User;
import com.shop.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    private PasswordEncoder encoder;

    public UserDto registerUser(RegistrationRequest registrationRequest){
        Optional<User> user = repository
                .findByEmail(registrationRequest.getEmail());
        if(user.isPresent()) throw new BadCredentialsException("Cannot signup, User already exists with email"+registrationRequest.getEmail());

        User mappedUser = modelMapper.map(registrationRequest,User.class);
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        mappedUser.setConfirmPassword(passwordEncoder.encode(mappedUser.getConfirmPassword()));

        //Save the user to database
        User savedUser = repository.save(mappedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(String email,String password){
        User user = repository.findByEmail(email).orElseThrow();
        if(encoder.matches(password,user.getPassword())){
//            return generateJWT(user);
        }
        throw new RuntimeException("Invalid credentials");
    }


}
