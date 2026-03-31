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

public class DashboardSummaryResponse {
    private BigDecimal revenueToday;
    private Long customersToday;
    private Integer totalTables;
    private Integer occupiedTables;
    private Double occupancyRate;
    private Long activeReservations;
}