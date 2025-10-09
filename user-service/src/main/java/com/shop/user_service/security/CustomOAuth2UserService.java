package com.shop.user_service.security;


import com.shop.user_service.Entity.User;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("locauser ======"+userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String,Object> attrs = oAuth2User.getAttributes();

        String email = (String) attrs.get("email");
        String name = (String) attrs.get("name");
        String providerId = (String) attrs.get("sub");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .provider("google")
                            .providerId(providerId)
                            .role("USER")
                            .build();
                    return userRepository.save(newUser);
                });

        // Update existing user if needed
        user.setName(name);
        user.setProvider("google");
        user.setProviderId(providerId);
        userRepository.save(user);

        // ✅ Return your custom OAuth2User with DB user info
        return new CustomOAuth2User(user, attrs);
    }

}