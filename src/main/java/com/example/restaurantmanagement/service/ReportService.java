package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.*;

import java.util.List;

public interface ReportService {
    ReportSummaryResponse getSummary(int year);
    List<MonthlyReportResponse> getMonthlyReport(int year);
    List<CategoryReportResponse> getCategoryReport(int year);
    List<BestSellerResponse> getBestSellers(int year, int limit);

    DashboardSummaryResponse getDashboardSummary();
    List<DailyRevenueResponse> getWeeklyRevenueTrend();
    List<PeakHourResponse> getPeakHoursToday();
}