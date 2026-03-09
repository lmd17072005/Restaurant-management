package com.example.restaurantmanagement.dto.request;

import com.example.restaurantmanagement.entity.enums.TableStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableRequest {
    @NotBlank(message = "Table code is required")
    @Size(max = 10)
    private String tableCode;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    private TableStatus status;
}

