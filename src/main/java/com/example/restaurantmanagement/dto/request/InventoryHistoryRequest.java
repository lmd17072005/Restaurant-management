package com.example.restaurantmanagement.dto.request;

import com.example.restaurantmanagement.entity.enums.InventoryTransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistoryRequest {
    @NotNull(message = "Ingredient ID is required")
    private Integer ingredientId;

    @NotNull(message = "Transaction type is required")
    private InventoryTransactionType transactionType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;

    private BigDecimal unitPrice;
}

