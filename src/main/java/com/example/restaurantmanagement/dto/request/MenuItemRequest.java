package com.example.restaurantmanagement.dto.request;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemRequest {
    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotBlank(message = "Menu item name is required")
    @Size(max = 150)
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Size(max = 500)
    private String imageUrl;

    private MenuItemStatus status;
}

