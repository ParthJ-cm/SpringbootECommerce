package com.shop.product_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveProductDTO {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Stock Quantity is required")
    private Integer stockQuantity;

    @Size(max = 255, message = "Image Url must be less than 255 characters")
    private String imageUrl;

    @NotNull(message = "Brand Id is required")
    private Long brandId;

    @NotEmpty(message = "At least one product variant is required")
    private List<SaveVariantDTO> variants;

    private List<Long> categoryIds;
}
