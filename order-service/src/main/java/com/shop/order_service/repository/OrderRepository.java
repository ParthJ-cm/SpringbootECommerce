package com.shop.order_service.repository;

import com.shop.order_service.entity.Order;
import com.shop.order_service.entity.OrderStatus;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders of a specific user
    List<Order> findAllByUserId(Long userId);

    // Find order by status
    List<Order> findByStatus(OrderStatus status);

    // Find all Orders with item
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems")
    List<Order> findAllWithItems();

    // Find all Orders with item (Paged)
    Page<Order> findAll(Pageable pageable);

    //Find the Order with all the items
    @EntityGraph(attributePaths = {"orderItems"})
    Optional<Order> findWithItemsById(Long id);

    // Count orders by status
    long countByStatus(OrderStatus status);

    // Find recent orders (e.g., last 30 days)
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate")
    List<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate);

//    @Modifying
//    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
//    int updateOrderStatus(@Param("id") Long id, @Param("status") OrderStatus status);
//
//    @Query("UPDATE Order o SET o.status = CANCELED WHERE o.id = :id")
//    boolean cancelOrderStatus(@Param("id") Long id);

//    // Find all Orders with item
//    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems")
//    List<Order> getAllOrdersWithItems();

  // for analytics (e.g., monthly revenue)
//    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
//    BigDecimal findTotalSalesByMonth(@Param("month") int month, @Param("year") int year);
}
