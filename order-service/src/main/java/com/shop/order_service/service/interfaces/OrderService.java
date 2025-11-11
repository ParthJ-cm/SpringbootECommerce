package com.shop.order_service.service.interfaces;

import com.shop.order_service.dto.OrderDTO;
import com.shop.order_service.dto.SaveOrderDTO;
import com.shop.order_service.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByUser(Long userId);
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    List<OrderDTO> getRecentOrders(int days);
    OrderDTO createOrder(SaveOrderDTO saveOrderDTO);
    OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
    OrderDTO cancelOrder(Long orderId);
    long countOrdersByStatus(OrderStatus status);
    List<OrderDTO> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end);
}
