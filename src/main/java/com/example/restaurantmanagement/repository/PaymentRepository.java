package com.example.restaurantmanagement.repository;
import com.example.restaurantmanagement.entity.Payment;
import com.example.restaurantmanagement.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    List<Payment> findByStatus(PaymentStatus status);
}