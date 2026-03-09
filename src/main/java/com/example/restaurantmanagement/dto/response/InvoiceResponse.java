package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {
    private Long id;
    private Integer tableId;
    private String tableCode;
    private Long customerId;
    private String customerName;
    private String invoiceCode;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private String discountReason;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private Long openedById;
    private String openedByName;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private List<OrderResponse> orders;
    private List<PaymentResponse> payments;
}

