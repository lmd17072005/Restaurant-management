package com.example.restaurantmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRequest {
    @NotNull(message = "Table ID is required")
    private Integer tableId;

    private Long customerId;

    private BigDecimal discount;

    @Size(max = 255)
    private String discountReason;
}

