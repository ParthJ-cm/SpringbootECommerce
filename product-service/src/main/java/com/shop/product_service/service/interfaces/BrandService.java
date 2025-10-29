package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.BrandDTO;
import com.shop.product_service.dto.SaveBrandDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {
    BrandDTO saveBrand(SaveBrandDTO saveBrandDTO);
    BrandDTO getBrand(Long id);
    void deleteBrand(Long id);
    Page<BrandDTO> paginatedBrands(String search, Pageable pageable);
}