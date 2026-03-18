package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.TopSellingItemResponse;
import com.example.restaurantmanagement.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('QUAN_LY')")
@Tag(name = "Statistics", description = "Statistics and reporting API for managers")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/top-selling/weekly")
    @Operation(summary = "Get top selling items this week")
    public ResponseEntity<ApiResponse<List<TopSellingItemResponse>>> getTopSellingThisWeek(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(
                statisticsService.getTopSellingItemsThisWeek(limit)));
    }

    @GetMapping("/top-selling/monthly")
    @Operation(summary = "Get top selling items this month")
    public ResponseEntity<ApiResponse<List<TopSellingItemResponse>>> getTopSellingThisMonth(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(
                statisticsService.getTopSellingItemsThisMonth(limit)));
    }

    @GetMapping("/top-selling/custom")
    @Operation(summary = "Get top selling items in custom date range")
    public ResponseEntity<ApiResponse<List<TopSellingItemResponse>>> getTopSellingCustomRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(
                statisticsService.getTopSellingItemsCustomRange(startDate, endDate, limit)));
    }
}
