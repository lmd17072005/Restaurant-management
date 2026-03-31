package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT " +
            "oi.menuItem.id, " +
            "oi.menuItem.name, " +
            "oi.menuItem.category.name, " +
            "oi.menuItem.imageUrl, " +
            "SUM(oi.quantity), " +
            "SUM(oi.subtotal) " +
            "FROM OrderItem oi " +
            "WHERE oi.status = com.example.restaurantmanagement.entity.enums.OrderItemStatus.hoan_thanh " +
            "AND oi.order.createdAt >= :startDate " +
            "AND oi.order.createdAt < :endDate " +
            "GROUP BY oi.menuItem.id, oi.menuItem.name, " +
            "oi.menuItem.category.name, oi.menuItem.imageUrl " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTopSellingItems(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT oi.menuItem.name, " +
            "SUM(oi.quantity), " +
            "SUM(oi.subtotal) " +
            "FROM OrderItem oi " +
            "WHERE oi.status = com.example.restaurantmanagement.entity.enums.OrderItemStatus.hoan_thanh " +
            "AND YEAR(oi.order.createdAt) = :year " +
            "GROUP BY oi.menuItem.id, oi.menuItem.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findBestSellers(@Param("year") int year, Pageable pageable);

    @Query("SELECT oi.menuItem.category.name, " +
            "SUM(oi.quantity), " +
            "SUM(oi.subtotal) " +
            "FROM OrderItem oi " +
            "WHERE oi.status = com.example.restaurantmanagement.entity.enums.OrderItemStatus.hoan_thanh " +
            "AND YEAR(oi.order.createdAt) = :year " +
            "GROUP BY oi.menuItem.category.id, oi.menuItem.category.name " +
            "ORDER BY SUM(oi.subtotal) DESC")
    List<Object[]> findRevenueByCategory(@Param("year") int year);
}

