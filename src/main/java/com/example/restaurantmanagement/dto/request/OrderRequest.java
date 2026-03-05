package com.example.restaurantmanagement.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderRequest {
    @NotNull(message = "Table ID is required")
    private Long tableId;
    private String note;
    @Valid
    @NotNull(message = "Order items are required")
    private List<OrderItemRequest> items;
}