package com.shop.product_service.service.implementations;

import com.shop.product_service.entity.Product;
import com.shop.product_service.repository.ProductRepository;
import com.shop.product_service.service.interfaces.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void deleteProductsByBrandId(Long brandId) {
        List<Product> products = productRepository.findByBrand_BrandId(brandId);
        products.forEach(product -> product.setIsDeleted(true));
        productRepository.saveAll(products);
    }
}