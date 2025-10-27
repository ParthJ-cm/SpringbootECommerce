package com.shop.cart_service.controller;

import com.shop.cart_service.dto.CartDTO;
import com.shop.cart_service.dto.CartItemDTO;
import com.shop.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDTO) {
        CartDTO updatedCart = cartService.addItemToCart(userId, cartItemDTO);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/user/{userId}/items/{productVariantId}")
    public ResponseEntity<CartDTO> updateItemQuantity(@PathVariable Long userId, @PathVariable Long productVariantId, @RequestParam Integer quantity) {
        CartDTO updatedCart = cartService.updateItemQuantity(userId, productVariantId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/user/{userId}/items/{productVariantId}")
    public ResponseEntity<CartDTO> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productVariantId) {
        CartDTO updatedCart = cartService.removeItemFromCart(userId, productVariantId);
        return ResponseEntity.ok(updatedCart);
    }
}