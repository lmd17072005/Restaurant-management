package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
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
    private Integer menuItemId;
    private String menuItemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String note;
}

