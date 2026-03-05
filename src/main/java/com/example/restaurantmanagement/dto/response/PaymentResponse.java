package com.example.restaurantmanagement.dto.response;
import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import com.example.restaurantmanagement.entity.enums.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentTime;
    private PaymentStatus status;
}