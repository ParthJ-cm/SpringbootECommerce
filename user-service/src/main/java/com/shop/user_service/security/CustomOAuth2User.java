package com.shop.user_service.security;

import com.shop.user_service.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final User user;
    private final Map<String,Object> attributes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getName() {
        return user.getEmail();
    }
}
