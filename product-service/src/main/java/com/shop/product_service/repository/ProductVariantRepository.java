package com.shop.product_service.repository;

import com.shop.product_service.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Query("SELECT v.sku FROM ProductVariant v WHERE v.sku IN :skus AND v.isDeleted = false")
    List<String> findExistingSkus(@Param("skus") List<String> skus);

    boolean existsByIdAndIsDeletedFalse(Long productVariantId);

    @Query("""
            SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END
            FROM ProductVariant v
            WHERE v.product.id =: productId
                    AND v.isPrimary = TRUE
                    AND v.isDeleted = FALSE
            """)
    boolean existsPrimaryVariant(Long productId);

}
