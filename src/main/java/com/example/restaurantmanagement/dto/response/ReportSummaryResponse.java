package com.example.restaurantmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ReportSummaryResponse {
    private BigDecimal totalRevenue;
    private BigDecimal avgOrderValue;
    private Long totalOrders;
}
