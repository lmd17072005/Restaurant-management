package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceCode(String invoiceCode);
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByTableId(Integer tableId);
    List<Invoice> findByCustomerId(Long customerId);

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i " +
            "WHERE i.status = com.example.restaurantmanagement.entity.enums.InvoiceStatus.da_thanh_toan " +
            "AND YEAR(i.closedAt) = :year")
    BigDecimal sumRevenueByYear(@Param("year") int year);

    @Query("SELECT COUNT(i) FROM Invoice i " +
            "WHERE i.status = com.example.restaurantmanagement.entity.enums.InvoiceStatus.da_thanh_toan " +
            "AND YEAR(i.closedAt) = :year")
    Long countPaidByYear(@Param("year") int year);

    @Query("SELECT MONTH(i.closedAt), SUM(i.totalAmount), COUNT(i) " +
            "FROM Invoice i " +
            "WHERE i.status = com.example.restaurantmanagement.entity.enums.InvoiceStatus.da_thanh_toan " +
            "AND YEAR(i.closedAt) = :year " +
            "GROUP BY MONTH(i.closedAt) " +
            "ORDER BY MONTH(i.closedAt)")
    List<Object[]> findMonthlyStats(@Param("year") int year);
}

