package com.example.restaurantmanagement.dto.request;

import com.example.restaurantmanagement.entity.enums.IngredientStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRequest {
    @NotBlank(message = "Ingredient name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Unit is required")
    @Size(max = 30)
    private String unit;

    @NotNull(message = "Stock quantity is required")
    private BigDecimal stockQuantity;

    @NotNull(message = "Min stock quantity is required")
    private BigDecimal minStockQuantity;

    private IngredientStatus status;
}

