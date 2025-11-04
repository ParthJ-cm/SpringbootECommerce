package com.shop.product_service.repository;

import com.shop.product_service.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByIdAndIsDeletedFalse(Long id);
    Optional<Brand> findByNameAndIsDeletedFalse(String name);
    Page<Brand> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
}
