package com.shop.cart_service.dto;

import com.shop.cart_service.model.Cart;
import com.shop.cart_service.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {
    public CartDTO toDto(Cart cart) {
        if (cart == null) return null;
        return new CartDTO(
                cart.getId(),
//                cart.getUser() != null ? cart.getUser().getId() : null,
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getCartItems().stream()
                        .map(this::toCartItemDto)
                        .collect(Collectors.toList())
        );
    }

    public CartItemDTO toCartItemDto(CartItem cartItem) {
        if (cartItem == null) return null;
        return new CartItemDTO(
                cartItem.getCartItemId(),
                cartItem.getCart() != null ? cartItem.getCart().getId() : null,
//                cartItem.getProduct() != null ? cartItem.getProduct().getId() : null, // Map productId
                cartItem.getQuantity(),
//                cartItem.getProduct() != null ? cartItem.getProduct().getPrice() : null, // Map price from Product
                cartItem.getCreatedBy(),
                cartItem.getUpdatedBy(),
                cartItem.getCreatedAt(),
                cartItem.getUpdatedAt()
        );
    }
}