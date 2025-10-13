package com.shop.order_service.controller;

import com.shop.order_service.dto.OrderItemDTO;
import com.shop.order_service.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    // Get all items belonging to a specific order
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getItemsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getItemsByOrderId(orderId));
    }

    // Add new item to a specific order
//    @PostMapping("/order/{orderId}")
//    public ResponseEntity<OrderItemDTO> addItemToOrder(
//            @PathVariable Long orderId,
//            @RequestBody OrderItemDTO itemDTO) {
//        OrderItemDTO created = orderItemService.addItemToOrder(orderId, itemDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }

    //  Update existing order item (quantity, unit price, etc.)
    @PutMapping("/{itemId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable Long itemId,
            @RequestBody OrderItemDTO dto) {
        return ResponseEntity.ok(orderItemService.updateOrderItem(itemId, dto));
    }

    // Delete order item
//    @DeleteMapping("/{itemId}")
//    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long itemId) {
//        orderItemService.deleteOrderItem(itemId);
//        return ResponseEntity.noContent().build();
//    }
}

