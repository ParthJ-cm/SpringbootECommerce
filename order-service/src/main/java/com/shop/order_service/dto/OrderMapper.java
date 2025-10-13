package com.shop.order_service.dto;

import com.shop.order_service.model.Order;
import com.shop.order_service.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toOrderDTO(Order order) {
        if (order == null) return null;

        List<OrderItemDTO> itemDTOs = order.getOrderItems() == null
                ? Collections.emptyList()
                : order.getOrderItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getCreatedBy(),
                order.getUpdatedBy(),
                itemDTOs
        );
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) return null;

        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getOrder() != null ? orderItem.getOrder().getId() : null,
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
        );
    }

    public Order toOrderEntity(OrderDTO orderDTO) {
        if (orderDTO == null) return null;

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setUserId(orderDTO.getUserId());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        order.setCreatedAt(orderDTO.getCreatedAt());
        order.setUpdatedAt(orderDTO.getUpdatedAt());
        order.setCreatedBy(orderDTO.getCreatedBy());
        order.setUpdatedBy(orderDTO.getUpdatedBy());

        if (orderDTO.getOrderItems() != null) {
            order.setOrderItems(
                    orderDTO.getOrderItems().stream()
                            .map(itemDTO -> toOrderItemEntity(itemDTO, order))
                            .collect(Collectors.toList())
            );
        }

        return order;
    }

    public OrderItem toOrderItemEntity(OrderItemDTO dto, Order order) {
        if (dto == null) return null;

        OrderItem item = new OrderItem();
        item.setId(dto.getId());
        item.setOrder(order);
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());
        return item;
    }
}
