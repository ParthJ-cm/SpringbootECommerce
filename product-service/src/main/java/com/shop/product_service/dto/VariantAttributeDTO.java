package com.shop.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeDTO {
    private Long id;
    private Long productVariantId;
    private AttributeDTO attribute;
}
