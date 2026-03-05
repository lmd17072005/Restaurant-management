package com.example.restaurantmanagement.dto.request;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TableRequest {
    @NotNull(message = "Table number is required")
    @Positive(message = "Table number must be positive")
    private Integer tableNumber;
    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;
    private TableStatus status;
}