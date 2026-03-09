package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByInvoiceId(Long invoiceId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCreatedById(Long userId);
}

