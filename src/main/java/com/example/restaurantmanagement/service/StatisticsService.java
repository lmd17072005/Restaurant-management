package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.TopSellingItemResponse;

import java.util.List;

public interface StatisticsService {

    List<TopSellingItemResponse> getTopSellingItemsThisWeek(int limit);

    List<TopSellingItemResponse> getTopSellingItemsThisMonth(int limit);

    List<TopSellingItemResponse> getTopSellingItemsCustomRange(
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate,
            int limit);
}
