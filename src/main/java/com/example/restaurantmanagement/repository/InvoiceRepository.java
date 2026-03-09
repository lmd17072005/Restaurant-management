package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceCode(String invoiceCode);
    List<Invoice> findByStatus(InvoiceStatus status);
    List<Invoice> findByTableId(Integer tableId);
    List<Invoice> findByCustomerId(Long customerId);
}

