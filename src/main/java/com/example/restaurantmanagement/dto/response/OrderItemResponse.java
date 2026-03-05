package com.example.restaurantmanagement.dto.response;
import lombok.*;
import java.math.BigDecimal;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemResponse {
    private Long id;
    private Long menuItemId;
    private String menuItemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String note;
}