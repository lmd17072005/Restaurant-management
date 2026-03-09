package com.example.restaurantmanagement.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponse {
    private Long id;
    private Integer menuItemId;
    private String menuItemName;
    private Integer ingredientId;
    private String ingredientName;
    private String ingredientUnit;
    private BigDecimal quantityRequired;
}

