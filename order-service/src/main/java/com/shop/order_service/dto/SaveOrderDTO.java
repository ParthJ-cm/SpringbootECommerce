package com.shop.order_service.dto;


import com.shop.order_service.entity.OrderStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveOrderDTO {
    @NotNull(message = "User Id should not be null")
    private Long userId;

    @NotNull(message = "Total amount should not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "Total amount should be greater than 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Order must have a Status")
    private OrderStatus status;

    @NotEmpty(message = "Order must have at least 1 item")
    @Size(min = 1, message = "Order should have at least 1 item")
    private List<SaveOrderItemDTO> orderItems;
}
