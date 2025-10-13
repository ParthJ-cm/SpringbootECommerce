package com.shop.cart_service.service;

import com.shop.cart_service.dto.CartDTO;
import com.shop.cart_service.dto.CartMapper;
import com.shop.cart_service.model.CartItem;
import com.shop.cart_service.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public Optional<CartDTO> getCartById(Long id) {
        return cartRepository.findCartWithItemsById(id)
                .map(cartMapper::toDto);
    }

    public List<CartDTO> getAllCarts() {
        return cartRepository.findAllCartsWithItems()
                .stream()
                .map(cartMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean deleteCart(Long id) {
        return cartRepository.findById(id)
                .map(cart -> {
                    cartRepository.delete(cart);
                    return true;
                })
                .orElse(false);
    }

    public Optional<CartDTO> updateCart(Long id, CartDTO cartDTO) {
        return cartRepository.findById(id)
                .map(cart -> {
                    // Update user if provided (uncomment and adjust if needed)
//                     cart.setUser(cartDTO.getUserId() != null ? new User(cartDTO.getUserId()) : cart.getUser());
                    cart.setUpdatedAt(LocalDateTime.now());
                    // Update cartItems (replace all items)
                     cart.getCartItems().clear(); // Uncomment if you want to replace all items
                    if (cartDTO.getCartItems() != null) {
                        cartDTO.getCartItems().forEach(itemDTO -> {
                            CartItem item = new CartItem();
                            item.setCart(cart); // Set the Cart relationship
                            // item.setProduct(new Product(itemDTO.getProductId())); // Uncomment if needed
                            item.setQuantity(itemDTO.getQuantity());
                            item.setCreatedBy(itemDTO.getCreatedBy());
                            item.setUpdatedBy(itemDTO.getUpdatedBy());
                            item.setCreatedAt(itemDTO.getCreatedAt() != null ? itemDTO.getCreatedAt() : LocalDateTime.now());
                            item.setUpdatedAt(LocalDateTime.now());
                            cart.getCartItems().add(item); // Add the new CartItem to the Cart
                        });
                    }
                    return cartRepository.save(cart);
                })
                .map(cartMapper::toDto);
    }
}
