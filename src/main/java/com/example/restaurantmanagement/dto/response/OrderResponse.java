package com.example.restaurantmanagement.dto.response;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponse {
    private Long id;
    private Long tableId;
    private Integer tableNumber;
    private String staffName;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String note;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}