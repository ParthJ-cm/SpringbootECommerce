package com.shop.order_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class SaveOrderItemDTO {

    @NotNull(message = "Order item should have a product variant id")
    private Long productVariantId;

    @NotEmpty(message = "Product name should not be empty")
    private String productName;

    @NotEmpty
    private String variantName;

    @Min(value = 1, message = "Product quantity should be at least 1")
    private int  quantity;

    @NotNull(message = "Price at purchase should not be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal priceAtPurchase;

    @NotNull(message = "Subtotal should not be null")
    @DecimalMin(value = "0.00", inclusive = false, message = "Subtotal must be greater than 0")
    private BigDecimal subTotal;
}
