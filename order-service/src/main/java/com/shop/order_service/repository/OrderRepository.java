package com.shop.order_service.repository;

import com.shop.order_service.model.Order;
import com.shop.order_service.model.OrderStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders of a specific user
    List<Order> findByUserId(Long userId);

    // Find order by status
    List<Order> findByStatus(OrderStatus status);

    // Count orders by status
    long countByStatus(OrderStatus status);

    // Find recent orders (e.g., last 30 days)
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate")
    List<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate);

    //Find the Order with all the items
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> getOrderWithItemsById(Long id);

    // Find all Orders with item
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems")
    List<Order> getAllOrdersWithItems();

//    // for analytics (e.g., monthly revenue)
//    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
//    BigDecimal findTotalSalesByMonth(@Param("month") int month, @Param("year") int year);
}
