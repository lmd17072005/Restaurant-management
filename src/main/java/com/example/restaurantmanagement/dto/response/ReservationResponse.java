package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private Integer tableId;
    private String tableCode;
    private Long customerId;
    private String customerName;
    private Integer numberOfGuests;
    private LocalDateTime reservationTime;
    private String note;
    private ReservationStatus status;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private Long confirmedById;
    private String confirmedByName;
    private LocalDateTime confirmedAt;
}

