package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.SaveVariantDTO;

import java.util.List;

public interface ValidateProductService {
    void validateAttribute(Long attributeId);
    void validateAttributes(List<Long> attributeIds);
    void validateProduct(Long productId);
    void validateProductVariant(Long productVariantId);
    void validateProductVariants(List<SaveVariantDTO> variantDTOs);
    void validatePrimaryVariant(List<SaveVariantDTO> variants);
}
