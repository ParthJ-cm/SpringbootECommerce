package com.shop.product_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveVariantDTO {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "SKU is required")
    private String sku;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock Quantity is required")
    private Integer stockQuantity = 0;

    @Size(max = 255, message = "Image Url must be less than 255 characters")
    private String imageUrl;

    private Boolean isPrimary;

    @NotEmpty(message = "At least one variant attribute is required")
    private List<SaveVariantAttributeDTO> variantAttributes;
}
