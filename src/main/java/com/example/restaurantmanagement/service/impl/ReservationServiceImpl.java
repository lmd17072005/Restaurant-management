package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.Reservation;
import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.ReservationMapper;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        return reservationMapper.toResponseList(reservationRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long id) {
        return reservationMapper.toResponse(reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByStatus(ReservationStatus status) {
        return reservationMapper.toResponseList(reservationRepository.findByStatus(status));
    }

    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", request.getTableId()));
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        if (request.getNumberOfGuests() > table.getCapacity()) {
            throw new BadRequestException("Number of guests exceeds table capacity");
        }

        User currentUser = getCurrentUser();

        Reservation reservation = Reservation.builder()
                .table(table)
                .customer(customer)
                .numberOfGuests(request.getNumberOfGuests())
                .reservationTime(request.getReservationTime())
                .note(request.getNote())
                .status(ReservationStatus.cho_xac_nhan)
                .createdBy(currentUser)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", request.getTableId()));
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));

        if (request.getNumberOfGuests() > table.getCapacity()) {
            throw new BadRequestException("Number of guests exceeds table capacity");
        }

        reservation.setTable(table);
        reservation.setCustomer(customer);
        reservation.setNumberOfGuests(request.getNumberOfGuests());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setNote(request.getNote());

        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        reservation.setStatus(status);

        if (status == ReservationStatus.da_xac_nhan) {
            User currentUser = getCurrentUser();
            reservation.setConfirmedBy(currentUser);
            reservation.setConfirmedAt(LocalDateTime.now());
        }

        // Table status update is handled by PostgreSQL trigger fn_dat_ban_cap_nhat_ban
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", "id", id);
        }
        reservationRepository.deleteById(id);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
}

