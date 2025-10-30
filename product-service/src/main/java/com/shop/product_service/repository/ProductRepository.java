package com.shop.product_service.repository;

import com.shop.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategories_Id(Long categoryId);
    List<Product> findByBrand_Id(Long brandId);
}