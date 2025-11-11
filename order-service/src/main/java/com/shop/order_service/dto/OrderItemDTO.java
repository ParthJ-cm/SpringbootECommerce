package com.shop.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long productVariantId;
    private String productName;
    private String variantName;
    private int quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subTotal;
}

