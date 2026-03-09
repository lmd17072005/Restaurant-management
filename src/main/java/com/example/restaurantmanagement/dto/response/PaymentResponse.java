package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long invoiceId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private Long processedById;
    private String processedByName;
    private LocalDateTime paymentDate;
}

