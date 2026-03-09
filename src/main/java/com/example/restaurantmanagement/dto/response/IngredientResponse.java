package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.IngredientStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientResponse {
    private Integer id;
    private String name;
    private String unit;
    private BigDecimal stockQuantity;
    private BigDecimal minStockQuantity;
    private IngredientStatus status;
    private LocalDateTime createdAt;
}

