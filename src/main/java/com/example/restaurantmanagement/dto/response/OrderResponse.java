package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long invoiceId;
    private OrderStatus status;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}

