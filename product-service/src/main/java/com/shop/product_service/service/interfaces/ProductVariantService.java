package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.ProductVariantDTO;
import com.shop.product_service.dto.SaveVariantDTO;

import java.util.List;

public interface ProductVariantService {
    List<ProductVariantDTO> create(Long productId, List<SaveVariantDTO> productVariantDTOS);

}
