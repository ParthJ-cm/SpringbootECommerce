package com.shop.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity = 0;
    private String imageUrl;
    private Boolean isPrimary;
    private List<SaveVariantAttributeDTO> variantAttributes;
}