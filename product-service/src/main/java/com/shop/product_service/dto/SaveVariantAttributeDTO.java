package com.shop.product_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveVariantAttributeDTO {

    @NotNull(message = "Attribute Id is required")
    private Long attributeId;

    @NotNull(message = "Attribute value is required")
    private String attributeValue;
}
