package com.example.restaurantmanagement.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryRequest {
    @NotBlank(message = "Category name is required")
    private String name;
    private String description;
}