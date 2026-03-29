package com.example.restaurantmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Jacksonized
public class ReservationRequest {
    @NotNull(message = "Table ID is required")
    private Integer tableId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Number of guests is required")
    @Positive(message = "Number of guests must be positive")
    private Integer numberOfGuests;

    @NotNull(message = "Reservation time is required")
    private LocalDateTime reservationTime;

    @Size(max = 500)
    private String note;
}
