package com.shop.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stockQuantity;
    private String imageUrl;
    private Boolean isActive;
    private List<ProductVariantDTO> productVariants;
    private List<ReviewDTO> reviews;
}