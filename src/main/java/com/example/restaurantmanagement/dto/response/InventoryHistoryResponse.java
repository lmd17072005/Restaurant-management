package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.InventoryTransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistoryResponse {
    private Long id;
    private Integer ingredientId;
    private String ingredientName;
    private InventoryTransactionType transactionType;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Long performerId;
    private String performerName;
    private LocalDateTime transactionDate;
}

