package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.SaveVariantAttributeDTO;
import com.shop.product_service.dto.VariantAttributeDTO;

import java.util.List;

public interface VariantAttributeService {
    List<VariantAttributeDTO> create(Long productVariantId, List<SaveVariantAttributeDTO> variantAttributeDTOs);
}
