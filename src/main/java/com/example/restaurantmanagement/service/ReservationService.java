package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;

import java.util.List;

public interface ReservationService {
    List<ReservationResponse> getAllReservations();
    ReservationResponse getReservationById(Long id);
    List<ReservationResponse> getReservationsByStatus(ReservationStatus status);
    ReservationResponse createReservation(ReservationRequest request);
    ReservationResponse updateReservation(Long id, ReservationRequest request);
    ReservationResponse updateReservationStatus(Long id, ReservationStatus status);
    void deleteReservation(Long id);
}

