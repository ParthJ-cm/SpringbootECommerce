package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.BrandDTO;
import com.shop.product_service.dto.SaveBrandDTO;

public interface BrandService {
    BrandDTO saveBrand(SaveBrandDTO createBrandDTO);
}
