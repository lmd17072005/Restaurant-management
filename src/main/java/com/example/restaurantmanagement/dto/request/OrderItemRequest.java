package com.example.restaurantmanagement.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemRequest {
    @NotNull(message = "Menu item ID is required")
    private Long menuItemId;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    private String note;
}