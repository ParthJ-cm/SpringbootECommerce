package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.ProductDTO;
import com.shop.product_service.dto.SaveProductDTO;

public interface ProductService {
    void deleteProductsByBrandId(Long brandId);
    ProductDTO createProduct(SaveProductDTO saveProductDTO);
}