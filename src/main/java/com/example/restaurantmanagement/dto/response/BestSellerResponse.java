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
public class BestSellerResponse {
    private Integer rank;
    private String name;
    private Long sales;
    private BigDecimal revenue;
    private Double percentage;
}
