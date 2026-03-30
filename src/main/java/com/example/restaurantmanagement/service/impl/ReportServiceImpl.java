package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.response.BestSellerResponse;
import com.example.restaurantmanagement.dto.response.CategoryReportResponse;
import com.example.restaurantmanagement.dto.response.MonthlyReportResponse;
import com.example.restaurantmanagement.dto.response.ReportSummaryResponse;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.OrderItemRepository;
import com.example.restaurantmanagement.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final InvoiceRepository invoiceRepository;
    private final OrderItemRepository orderItemRepository;

    private static final String[] MONTH_LABELS =
            {"Jan","Feb","Mar","Apr","May","Jun",
                    "Jul","Aug","Sep","Oct","Nov","Dec"};

    @Override
    @Transactional(readOnly = true)
    public ReportSummaryResponse getSummary(int year) {
        BigDecimal totalRevenue = invoiceRepository.sumRevenueByYear(year);
        Long totalOrders = invoiceRepository.countPaidByYear(year);
        BigDecimal avgOrderValue = totalOrders > 0
                ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return ReportSummaryResponse.builder()
                .totalRevenue(totalRevenue)
                .avgOrderValue(avgOrderValue)
                .totalOrders(totalOrders)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyReportResponse> getMonthlyReport(int year) {
        List<Object[]> rows = invoiceRepository.findMonthlyStats(year);
        return rows.stream().map(row -> MonthlyReportResponse.builder()
                .month(MONTH_LABELS[((Number) row[0]).intValue() - 1])
                .revenue((BigDecimal) row[1])
                .orders(((Number) row[2]).longValue())
                .build()
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryReportResponse> getCategoryReport(int year) {
        List<Object[]> rows = orderItemRepository.findRevenueByCategory(year);

        BigDecimal total = rows.stream()
                .map(r -> (BigDecimal) r[2])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return rows.stream().map(row -> {
            BigDecimal revenue = (BigDecimal) row[2];
            double pct = total.compareTo(BigDecimal.ZERO) > 0
                    ? revenue.divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue()
                    : 0.0;
            return CategoryReportResponse.builder()
                    .categoryName((String) row[0])
                    .sales(((Number) row[1]).longValue())
                    .revenue(revenue)
                    .percentage(Math.round(pct * 10.0) / 10.0)
                    .build();
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BestSellerResponse> getBestSellers(int year, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> rows = orderItemRepository.findBestSellers(year, pageable);

        BigDecimal totalRevenue = rows.stream()
                .map(r -> (BigDecimal) r[2])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<BestSellerResponse> result = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);
            BigDecimal revenue = (BigDecimal) row[2];
            double pct = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? revenue.divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue()
                    : 0.0;
            result.add(BestSellerResponse.builder()
                    .rank(i + 1)
                    .name((String) row[0])
                    .sales(((Number) row[1]).longValue())
                    .revenue(revenue)
                    .percentage(Math.round(pct * 10.0) / 10.0)
                    .build());
        }
        return result;
    }
}