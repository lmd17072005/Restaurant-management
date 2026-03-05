package com.example.restaurantmanagement.dto.response;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private Long tableId;
    private Integer tableNumber;
    private LocalDateTime reservationTime;
    private Integer numberOfGuests;
    private ReservationStatus status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}