package com.shop.order_service.service.implementations;

import com.shop.order_service.dto.OrderDTO;
import com.shop.order_service.dto.OrderItemDTO;
import com.shop.order_service.exceptions.ResourceNotFoundException;
import com.shop.order_service.entity.Order;
import com.shop.order_service.entity.OrderItem;
import com.shop.order_service.repository.OrderItemRepository;
import com.shop.order_service.repository.OrderRepository;
import com.shop.order_service.service.interfaces.OrderItemsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemsService {
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    public List<OrderItemDTO> getItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                .collect(Collectors.toList());
    }

//    public OrderItemDTO addItemToOrder(Long orderId, OrderItemDTO dto) {
//        OrderItem orderItem = orderMapper.toOrderItemEntity(dto, null);
//        orderItem.setOrder(Order.builder().id(orderId).build());
//        OrderItem saved = orderItemRepository.save(orderItem);
//        return orderMapper.toOrderItemDTO(saved);
//    }
//
//    public OrderItemDTO updateOrderItem(Long orderId, OrderItemDTO dto) {
//        OrderItem orderItem = orderItemRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + orderId));
//        orderItem.setQuantity(dto.getQuantity());
//        orderItem.setUnitPrice(dto.getUnitPrice());
//        OrderItem saved = orderItemRepository.save(orderItem);
//        return orderMapper.toOrderItemDTO(saved);
//    }
//
//    public void deleteOrderItem(Long itemId) {
//        orderItemRepository.deleteById(itemId);
//    }
}

