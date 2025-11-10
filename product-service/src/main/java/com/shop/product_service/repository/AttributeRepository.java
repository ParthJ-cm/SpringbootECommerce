package com.shop.product_service.repository;

import com.shop.product_service.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @Query("SELECT a.id FROM Attribute a WHERE a.id IN :ids AND a.isDeleted = false")
    List<Long> findExistingIds(@Param("ids") List<Long> ids);

}