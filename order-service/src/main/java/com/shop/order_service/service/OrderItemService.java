package com.shop.order_service.service;

import com.shop.order_service.dto.OrderItemDTO;
import com.shop.order_service.dto.OrderMapper;
import com.shop.order_service.exception.ResourceNotFoundException;
import com.shop.order_service.model.Order;
import com.shop.order_service.model.OrderItem;
import com.shop.order_service.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    public List<OrderItemDTO> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(orderMapper::toOrderItemDTO)
                .collect(Collectors.toList());
    }

    public OrderItemDTO addItemToOrder(Long orderId, OrderItemDTO dto) {
        OrderItem orderItem = orderMapper.toOrderItemEntity(dto, null);
        orderItem.setOrder(Order.builder().id(orderId).build());
        OrderItem saved = orderItemRepository.save(orderItem);
        return orderMapper.toOrderItemDTO(saved);
    }

    public OrderItemDTO updateOrderItem(Long orderId, OrderItemDTO dto) {
        OrderItem orderItem = orderItemRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + orderId));
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setUnitPrice(dto.getUnitPrice());
        OrderItem saved = orderItemRepository.save(orderItem);
        return orderMapper.toOrderItemDTO(saved);
    }

    public void deleteOrderItem(Long itemId) {
        orderItemRepository.deleteById(itemId);
    }
}

