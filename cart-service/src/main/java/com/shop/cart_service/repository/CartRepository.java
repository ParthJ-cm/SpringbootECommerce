package com.shop.cart_service.repository;

import com.shop.cart_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems WHERE c.id = :id")
    Optional<Cart> findCartWithItemsById(Long id);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems")
    List<Cart> findAllCartsWithItems();

//    // Optional: Add if needed for user-specific carts
//    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems WHERE c.user.id = :userId")
//    List<Cart> findCartsByUserId(@Param("userId") Long userId);

}
