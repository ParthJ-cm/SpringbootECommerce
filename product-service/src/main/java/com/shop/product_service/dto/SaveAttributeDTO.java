package com.shop.product_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveAttributeDTO {

    private Long id;

    @NotNull(message = "Attribute name is required")
    @Size(max = 100,message = "Attribute name must be less than 100 characters")
    private String name;

    @Size(max = 50, message = "Unit must be less than 50 characters")
    private String unit;

    private Long categoryId;

}
