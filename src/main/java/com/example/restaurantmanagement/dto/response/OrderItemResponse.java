package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private Integer menuItemId;
    private String menuItemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String note;
    private OrderItemStatus status;
}

