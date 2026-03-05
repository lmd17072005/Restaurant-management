package com.example.restaurantmanagement.service.impl;
import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.Reservation;
import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.ReservationMapper;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
import com.example.restaurantmanagement.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantTableRepository tableRepository;
    private final ReservationMapper reservationMapper;
    @Override public List<ReservationResponse> getAllReservations() { return reservationMapper.toResponseList(reservationRepository.findAll()); }
    @Override public ReservationResponse getReservationById(Long id) { return reservationMapper.toResponse(reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id))); }
    @Override public List<ReservationResponse> getReservationsByStatus(ReservationStatus status) { return reservationMapper.toResponseList(reservationRepository.findByStatus(status)); }
    @Override @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId()).orElseThrow(() -> new ResourceNotFoundException("Table", "id", request.getTableId()));
        if (request.getNumberOfGuests() > table.getCapacity()) throw new BadRequestException("Number of guests exceeds table capacity");
        Reservation reservation = Reservation.builder().customerName(request.getCustomerName()).customerPhone(request.getCustomerPhone()).table(table).reservationTime(request.getReservationTime()).numberOfGuests(request.getNumberOfGuests()).status(ReservationStatus.PENDING).note(request.getNote()).build();
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }
    @Override @Transactional
    public ReservationResponse updateReservation(Long id, ReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        RestaurantTable table = tableRepository.findById(request.getTableId()).orElseThrow(() -> new ResourceNotFoundException("Table", "id", request.getTableId()));
        if (request.getNumberOfGuests() > table.getCapacity()) throw new BadRequestException("Number of guests exceeds table capacity");
        reservation.setCustomerName(request.getCustomerName()); reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setTable(table); reservation.setReservationTime(request.getReservationTime());
        reservation.setNumberOfGuests(request.getNumberOfGuests()); reservation.setNote(request.getNote());
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }
    @Override @Transactional
    public ReservationResponse updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "id", id));
        reservation.setStatus(status);
        if (status == ReservationStatus.CANCELLED || status == ReservationStatus.COMPLETED) {
            RestaurantTable table = reservation.getTable(); table.setStatus(TableStatus.AVAILABLE); tableRepository.save(table);
        }
        return reservationMapper.toResponse(reservationRepository.save(reservation));
    }
    @Override @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) throw new ResourceNotFoundException("Reservation", "id", id);
        reservationRepository.deleteById(id);
    }
}