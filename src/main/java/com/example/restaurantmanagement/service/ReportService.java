package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.BestSellerResponse;
import com.example.restaurantmanagement.dto.response.CategoryReportResponse;
import com.example.restaurantmanagement.dto.response.MonthlyReportResponse;
import com.example.restaurantmanagement.dto.response.ReportSummaryResponse;

import java.util.List;

public interface ReportService {
    ReportSummaryResponse getSummary(int year);
    List<MonthlyReportResponse> getMonthlyReport(int year);
    List<CategoryReportResponse> getCategoryReport(int year);
    List<BestSellerResponse> getBestSellers(int year, int limit);
}