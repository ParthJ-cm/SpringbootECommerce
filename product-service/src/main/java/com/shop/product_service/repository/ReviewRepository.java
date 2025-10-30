package com.shop.product_service.repository;

import com.shop.product_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByProduct_IdAndUserId(long productId, Long userId);
}