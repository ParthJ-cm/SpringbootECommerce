package com.shop.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long cartId; // Only the ID, not the full Cart object
//    private Long productId;
    private BigDecimal quantity;
//    private BigDecimal price; //getting from product
    private long createdBy;
    private long updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
