package com.shop.order_service.service;

import com.shop.order_service.dto.OrderDTO;
import com.shop.order_service.dto.OrderMapper;
import com.shop.order_service.exception.ResourceNotFoundException;
import com.shop.order_service.model.Order;
import com.shop.order_service.model.OrderStatus;
import com.shop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.getOrderWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toOrderDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toOrderEntity(orderDTO); // ✅ Correct method
        order.setStatus(OrderStatus.ORDERED);
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setCreatedBy(orderDTO.getCreatedBy());
        order.setUpdatedBy(orderDTO.getUpdatedBy());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(savedOrder);
    }

    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        order.setUpdatedAt(LocalDateTime.now());
        order.setUpdatedBy(orderDTO.getUpdatedBy());

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDTO(updatedOrder);
    }

    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getRecentOrders(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return orderRepository.findRecentOrders(startDate)
                .stream()
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }

    public long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    public List<OrderDTO> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(start) && o.getCreatedAt().isBefore(end))
                .map(orderMapper::toOrderDTO)
                .collect(Collectors.toList());
    }
}
