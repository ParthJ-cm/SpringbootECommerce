package com.shop.user_service.repository;

import com.shop.user_service.entity.User;
import com.shop.user_service.type.AuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User getUserByUserId(Long userId);
    Optional<User> findByProviderIdAndProvider(String providerId, AuthProviderType providerType);
}
