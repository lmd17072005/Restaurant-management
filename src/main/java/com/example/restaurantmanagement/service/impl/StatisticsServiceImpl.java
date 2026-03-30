package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.response.TopSellingItemResponse;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.repository.OrderItemRepository;
import com.example.restaurantmanagement.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderItemRepository orderItemRepository;
    private static final int MAX_LIMIT = 50;

    @Override
    public List<TopSellingItemResponse> getTopSellingItemsThisWeek(int limit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .toLocalDate().atStartOfDay();
        LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);

        return getTopSellingItems(startOfWeek, endOfWeek, limit);
    }

    @Override
    public List<TopSellingItemResponse> getTopSellingItemsThisMonth(int limit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.with(TemporalAdjusters.firstDayOfMonth())
                .toLocalDate().atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

        return getTopSellingItems(startOfMonth, endOfMonth, limit);
    }

    @Override
    public List<TopSellingItemResponse> getTopSellingItemsCustomRange(
            LocalDateTime startDate, LocalDateTime endDate, int limit) {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("startDate must be before endDate");
        }
        return getTopSellingItems(startDate, endDate, limit);
    }

    private List<TopSellingItemResponse> getTopSellingItems(
            LocalDateTime startDate, LocalDateTime endDate, int limit) {

        int safeLimit = Math.min(limit, MAX_LIMIT);

        List<Object[]> results = orderItemRepository.findTopSellingItems(
                startDate, endDate, PageRequest.of(0, limit)
        );

        AtomicInteger rank = new AtomicInteger(1);
        return results.stream()
                .map(row -> TopSellingItemResponse.builder()
                        .rank(rank.getAndIncrement())
                        .menuItemId((Integer) row[0])
                        .menuItemName((String) row[1])
                        .categoryName((String) row[2])
                        .imageUrl((String) row[3])
                        .totalQuantitySold((Long) row[4])
                        .totalRevenue((BigDecimal) row[5])
                        .build()).toList();
    }
}
