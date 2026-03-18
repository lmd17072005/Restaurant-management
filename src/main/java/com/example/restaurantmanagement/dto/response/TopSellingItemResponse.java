package com.example.restaurantmanagement.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopSellingItemResponse {
    private Integer rank;
    private Integer menuItemId;
    private String menuItemName;
    private String categoryName;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
    private String imageUrl;
}
