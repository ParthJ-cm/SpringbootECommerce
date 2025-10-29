package com.shop.cart_service.service;

import com.shop.cart_service.dto.CartDTO;
import com.shop.cart_service.dto.CartItemDTO;
import com.shop.cart_service.entity.Cart;
import com.shop.cart_service.entity.CartItem;
import com.shop.cart_service.repository.CartRepository;
import com.shop.cart_service.util.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });
        return cartMapper.toDto(cart);
    }

    public CartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProductVariantId(cartItemDTO.getProductVariantId());
        cartItem.setQuantity(cartItemDTO.getQuantity() != null ? cartItemDTO.getQuantity() : 1);

        cart.getCartItems().add(cartItem);
        cart.setUpdatedAt(LocalDateTime.now());
        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toDto(updatedCart);
    }

    public CartDTO updateItemQuantity(Long userId, Long productVariantId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found for product variant: " + productVariantId));

        cartItem.setQuantity(quantity);
        cart.setUpdatedAt(LocalDateTime.now());
        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toDto(updatedCart);
    }

    public CartDTO removeItemFromCart(Long userId, Long productVariantId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        cart.getCartItems().removeIf(item -> item.getProductVariantId().equals(productVariantId));
        cart.setUpdatedAt(LocalDateTime.now());
        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toDto(updatedCart);
    }
}