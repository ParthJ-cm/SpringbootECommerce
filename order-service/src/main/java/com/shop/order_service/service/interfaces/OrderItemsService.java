package com.shop.order_service.service.interfaces;

import com.shop.order_service.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemsService {
    List<OrderItemDTO> getItemsByOrderId(Long orderId);
}
