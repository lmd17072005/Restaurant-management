package com.example.restaurantmanagement.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationRequest {
    @NotBlank(message = "Customer name is required")
    private String customerName;
    @NotBlank(message = "Customer phone is required")
    private String customerPhone;
    @NotNull(message = "Table ID is required")
    private Long tableId;
    @NotNull(message = "Reservation time is required")
    private LocalDateTime reservationTime;
    @NotNull(message = "Number of guests is required")
    @Positive(message = "Number of guests must be positive")
    private Integer numberOfGuests;
    private String note;
}