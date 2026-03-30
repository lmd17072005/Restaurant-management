package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.response.*;
import com.example.restaurantmanagement.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Report & Analytics API")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get summary statistics")
    public ResponseEntity<ApiResponse<ReportSummaryResponse>> getSummary(
            @RequestParam(defaultValue = "0") int year) {
        int y = year == 0 ? LocalDate.now().getYear() : year;
        return ResponseEntity.ok(ApiResponse.success(reportService.getSummary(y)));
    }

    @GetMapping("/monthly")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get monthly revenue and orders")
    public ResponseEntity<ApiResponse<List<MonthlyReportResponse>>> getMonthly(
            @RequestParam(defaultValue = "0") int year) {
        int y = year == 0 ? LocalDate.now().getYear() : year;
        return ResponseEntity.ok(ApiResponse.success(reportService.getMonthlyReport(y)));
    }

    @GetMapping("/by-category")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get revenue by category")
    public ResponseEntity<ApiResponse<List<CategoryReportResponse>>> getByCategory(
            @RequestParam(defaultValue = "0") int year) {
        int y = year == 0 ? LocalDate.now().getYear() : year;
        return ResponseEntity.ok(ApiResponse.success(reportService.getCategoryReport(y)));
    }

    @GetMapping("/best-sellers")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get best selling dishes")
    public ResponseEntity<ApiResponse<List<BestSellerResponse>>> getBestSellers(
            @RequestParam(defaultValue = "0") int year,
            @RequestParam(defaultValue = "5") int limit) {
        int y = year == 0 ? LocalDate.now().getYear() : year;
        return ResponseEntity.ok(ApiResponse.success(reportService.getBestSellers(y, limit)));
    }
}