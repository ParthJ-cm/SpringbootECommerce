package com.shop.user_service.repository;

import com.shop.user_service.Entity.DemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemoRepo extends JpaRepository<DemoEntity,Long> {
}
