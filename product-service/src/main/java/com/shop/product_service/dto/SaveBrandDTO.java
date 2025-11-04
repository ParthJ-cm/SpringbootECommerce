package com.shop.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveBrandDTO {

    private Long id;

    @NotBlank(message = "Brand name is required")
    @Size(max = 100, message = "Brand name must be less than 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Size(max = 250, message = "Logo URL must be less than 250 characters")
    private String logoUrl;

}