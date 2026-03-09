package com.example.restaurantmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeRequest {
    @NotNull(message = "Menu item ID is required")
    private Integer menuItemId;

    @NotNull(message = "Ingredient ID is required")
    private Integer ingredientId;

    @NotNull(message = "Quantity required is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantityRequired;
}

