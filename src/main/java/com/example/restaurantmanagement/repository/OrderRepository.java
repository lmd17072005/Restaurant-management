package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByInvoiceId(Long invoiceId);
    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT " +
            "o.menuItem.id, " +
            "o.menuItem.name, " +
            "o.menuItem.category.name, " +
            "o.menuItem.imageUrl, " +
            "SUM(o.quantity), " +
            "SUM(o.subtotal) " +
            "FROM Order o " +
            "WHERE o.status = com.example.restaurantmanagement.entity.enums.OrderStatus.da_phuc_vu " +
            "AND o.createdAt >= :startDate " +
            "AND o.createdAt < :endDate " +
            "GROUP BY o.menuItem.id, o.menuItem.name, " +
            "o.menuItem.category.name, o.menuItem.imageUrl " +
            "ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findTopSellingItems(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT o.menuItem.name, SUM(o.quantity), SUM(o.subtotal) " +
            "FROM Order o " +
            "WHERE o.status = com.example.restaurantmanagement.entity.enums.OrderStatus.da_phuc_vu " +
            "AND YEAR(o.createdAt) = :year " +
            "GROUP BY o.menuItem.id, o.menuItem.name " +
            "ORDER BY SUM(o.quantity) DESC")
    List<Object[]> findBestSellers(@Param("year") int year, Pageable pageable);

    @Query("SELECT o.menuItem.category.name, SUM(o.quantity), SUM(o.subtotal) " +
            "FROM Order o " +
            "WHERE o.status = com.example.restaurantmanagement.entity.enums.OrderStatus.da_phuc_vu " +
            "AND YEAR(o.createdAt) = :year " +
            "GROUP BY o.menuItem.category.id, o.menuItem.category.name " +
            "ORDER BY SUM(o.subtotal) DESC")
    List<Object[]> findRevenueByCategory(@Param("year") int year);
}

