package com.example.restaurantmanagement.dto.request;
import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequest {
    @NotNull(message = "Order ID is required")
    private Long orderId;
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}