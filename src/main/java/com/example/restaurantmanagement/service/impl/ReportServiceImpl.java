package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.response.*;
import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
import com.example.restaurantmanagement.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final RestaurantTableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    private static final String[] MONTH_LABELS =
            {"Jan","Feb","Mar","Apr","May","Jun",
                    "Jul","Aug","Sep","Oct","Nov","Dec"};

    private static final String[] DAY_LABELS =
            {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    private static final String[] HOUR_LABELS = {
            "12 AM","1 AM","2 AM","3 AM","4 AM","5 AM",
            "6 AM","7 AM","8 AM","9 AM","10 AM","11 AM",
            "12 PM","1 PM","2 PM","3 PM","4 PM","5 PM",
            "6 PM","7 PM","8 PM","9 PM","10 PM","11 PM"
    };

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
        List<Object[]> rows = orderRepository.findRevenueByCategory(year);

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
        List<Object[]> rows = orderRepository.findBestSellers(year, pageable);

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

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryResponse getDashboardSummary() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        BigDecimal revenueToday = invoiceRepository
                .findDailyRevenue(startOfDay)
                .stream()
                .filter(row -> {
                    LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
                    return date.equals(LocalDate.now());
                })
                .map(row -> (BigDecimal) row[1])
                .findFirst()
                .orElse(BigDecimal.ZERO);

        Long customersToday = invoiceRepository.countInvoicesToday(startOfDay, endOfDay);

        List<RestaurantTable> allTables = tableRepository.findAll();
        int total = allTables.size();
        long occupied = allTables.stream()
                .filter(t -> t.getStatus() == TableStatus.dang_phuc_vu)
                .count();
        double rate = total > 0
                ? Math.round((occupied * 100.0 / total) * 10.0) / 10.0
                : 0.0;

        Long activeReservations = reservationRepository
                .countActiveReservationsToday(startOfDay, endOfDay);

        return DashboardSummaryResponse.builder()
                .revenueToday(revenueToday)
                .customersToday(customersToday)
                .totalTables(total)
                .occupiedTables((int) occupied)
                .occupancyRate(rate)
                .activeReservations(activeReservations)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyRevenueResponse> getWeeklyRevenueTrend() {
        LocalDateTime sevenDaysAgo = LocalDate.now().minusDays(6).atStartOfDay();
        List<Object[]> rows = invoiceRepository.findDailyRevenue(sevenDaysAgo);

        Map<LocalDate, BigDecimal> revenueMap = rows.stream().collect(
                Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> (BigDecimal) row[1]
                )
        );

        List<DailyRevenueResponse> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String label = date.getDayOfWeek().getDisplayName(
                    java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
            result.add(DailyRevenueResponse.builder()
                    .date(label)
                    .revenue(revenueMap.getOrDefault(date, BigDecimal.ZERO))
                    .build());
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PeakHourResponse> getPeakHoursToday() {
        List<Object[]> rows = invoiceRepository.findPeakHoursToday();
        return rows.stream().map(row -> PeakHourResponse.builder()
                .hour(HOUR_LABELS[((Number) row[0]).intValue()])
                .count(((Number) row[1]).longValue())
                .build()
        ).toList();
    }
}