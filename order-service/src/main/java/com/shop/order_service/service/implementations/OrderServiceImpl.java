package com.shop.order_service.service.implementations;

import com.shop.order_service.dto.OrderDTO;
import com.shop.order_service.dto.SaveOrderDTO;
import com.shop.order_service.exceptions.ResourceNotFoundException;
import com.shop.order_service.entity.Order;
import com.shop.order_service.entity.OrderStatus;
import com.shop.order_service.kafka.OrderProducer;
import com.shop.order_service.repository.OrderRepository;
import com.shop.order_service.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderProducer orderProducer;

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO createOrder(SaveOrderDTO saveOrderDTO) {
        Order order = modelMapper.map(saveOrderDTO, Order.class);
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        orderProducer.orderCreatedEvent(orderDTO);

        return orderDTO;
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        OrderDTO orderDTO = modelMapper.map(updatedOrder, OrderDTO.class);
        if (newStatus == OrderStatus.CANCELED) {
            orderProducer.orderStatusUpdatedEvent(orderDTO);
        }
        return orderDTO;
    }

    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(OrderStatus.CANCELED);
        Order updatedOrder = orderRepository.save(order);

        OrderDTO orderDTO = modelMapper.map(updatedOrder, OrderDTO.class);

        orderProducer.orderStatusUpdatedEvent(orderDTO);

        return orderDTO;
    }


    @Override
    public List<OrderDTO> getRecentOrders(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return orderRepository.findRecentOrders(startDate)
                .stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public List<OrderDTO> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(start) && o.getCreatedAt().isBefore(end))
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }
}
