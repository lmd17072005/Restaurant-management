package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import com.example.restaurantmanagement.service.ReservationService;
import com.example.restaurantmanagement.entity.enums.NotificationMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Reservation management API")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getAllReservations()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID")
    public ResponseEntity<ApiResponse<ReservationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getReservationById(id)));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reservations by status")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getByStatus(@PathVariable ReservationStatus status) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getReservationsByStatus(status)));
    }

    @PostMapping
    @Operation(summary = "Create reservation")
    public ResponseEntity<ApiResponse<ReservationResponse>> create(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(NotificationMessage.RESERVATION_CREATED_SUCCESS, reservationService.createReservation(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update reservation")
    public ResponseEntity<ApiResponse<ReservationResponse>> update(@PathVariable Long id, @Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.RESERVATION_UPDATED_SUCCESS, reservationService.updateReservation(id, request)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update reservation status")
    public ResponseEntity<ApiResponse<ReservationResponse>> updateStatus(@PathVariable Long id, @RequestParam ReservationStatus status) {
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.RESERVATION_STATUS_UPDATED_SUCCESS, reservationService.updateReservationStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Delete reservation")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.RESERVATION_DELETED_SUCCESS, null));
    }
}

