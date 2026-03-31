package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceCode(String invoiceCode);
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByTableId(Integer tableId);
    List<Invoice> findByCustomerId(Long customerId);

    /**
     * Find the latest unpaid invoice for a given table.
     * Uses native SQL with explicit enum cast to avoid Hibernate JPQL enum type mismatch.
     */
    @Query(value = """
            SELECT * FROM hoa_don
            WHERE ban_id = :tableId
              AND trang_thai = CAST('chua_thanh_toan' AS trang_thai_hoa_don_enum)
            ORDER BY ngay_tao DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Invoice> findLatestOpenInvoiceByTableId(@Param("tableId") Integer tableId);
}

