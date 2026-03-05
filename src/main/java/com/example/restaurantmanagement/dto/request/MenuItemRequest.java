package com.example.restaurantmanagement.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuItemRequest {
    @NotBlank(message = "Menu item name is required")
    private String name;
    private String description;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    private String imageUrl;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private Boolean isAvailable;
}