package com.shop.order_service.controller;

import com.shop.order_service.dto.OrderDTO;
import com.shop.order_service.dto.SaveOrderDTO;
import com.shop.order_service.entity.OrderStatus;
import com.shop.order_service.service.implementations.OrderServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5174")
public class OrderController {

    private final OrderServiceImpl orderService;

    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Get a single order by ID (with items)
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Get all orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }

    // Get all orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    // Create new order
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody SaveOrderDTO saveOrderDTO) {
        OrderDTO createdOrder = orderService.createOrder(saveOrderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

//    // Update order (status)
//    @PutMapping("/{orderId}")
//    public ResponseEntity<OrderDTO> updateOrder(
//            @PathVariable Long orderId,
//            @RequestBody OrderDTO orderDTO) {
//        return ResponseEntity.ok(orderService.updateOrder(orderId, orderDTO));
//    }

    // Update the order status
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // Cancel the order
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

//    // Soft delete (cancel) an order
//    @DeleteMapping("/{orderId}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
//        orderService.deleteOrder(orderId);
//        return ResponseEntity.noContent().build();
//    }

    // Get recent orders (last N days)
    @GetMapping("/recent")
    public ResponseEntity<List<OrderDTO>> getRecentOrders(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(orderService.getRecentOrders(days));
    }

    // Count orders by status
    @GetMapping("/count")
    public ResponseEntity<Long> countOrdersByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.countOrdersByStatus(status));
    }

    // Get orders between two dates
    @GetMapping("/between")
    public ResponseEntity<List<OrderDTO>> getOrdersBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(orderService.getOrdersBetweenDates(start, end));
    }
}
