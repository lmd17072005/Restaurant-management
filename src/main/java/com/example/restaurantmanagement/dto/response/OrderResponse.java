package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Response cho một dòng đơn hàng (don_hang) — mỗi dòng = 1 món ăn */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long invoiceId;
    private Integer menuItemId;
    private String menuItemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String note;
    private OrderStatus status;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
