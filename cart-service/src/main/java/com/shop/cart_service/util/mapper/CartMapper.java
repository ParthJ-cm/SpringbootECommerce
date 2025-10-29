package com.shop.cart_service.util.mapper;

import com.shop.cart_service.dto.CartDTO;
import com.shop.cart_service.dto.CartItemDTO;
import com.shop.cart_service.entity.Cart;
import com.shop.cart_service.entity.CartItem;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    private final ModelMapper modelMapper;

    public CartMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CartDTO toDto(Cart cart) {
        CartDTO dto = modelMapper.map(cart, CartDTO.class);

        List<CartItemDTO> itemDtos = cart.getCartItems().stream()
                .map(item -> modelMapper.map(item, CartItemDTO.class))
                .collect(Collectors.toList());

        dto.setCartItems(itemDtos);
        return dto;
    }
}