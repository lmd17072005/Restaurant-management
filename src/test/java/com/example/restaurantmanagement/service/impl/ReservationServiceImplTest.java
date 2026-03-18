package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.ReservationRequest;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.Reservation;
import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.ReservationMapper;
import com.example.restaurantmanagement.repository.ReservationRepository;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
import com.example.restaurantmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private RestaurantTableRepository tableRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationMapper reservationMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User mockUser;
    private RestaurantTable mockTable;
    private Reservation mockReservation;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        mockTable = new RestaurantTable();
        mockTable.setId(1);
        mockTable.setCapacity(4);
        mockTable.setStatus(TableStatus.trong);

        mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setTable(mockTable);
        mockReservation.setCustomer(mockUser);
        mockReservation.setStatus(ReservationStatus.cho_xac_nhan);
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    void createReservation_Success() {
        // Arrange
        setupSecurityContext();
        ReservationRequest request = new ReservationRequest();
        request.setTableId(1);
        request.setCustomerId(1L);
        request.setNumberOfGuests(3);
        request.setReservationTime(LocalDateTime.now().plusDays(1));

        when(tableRepository.findById(1)).thenReturn(Optional.of(mockTable));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(new ReservationResponse());

        // Act
        ReservationResponse response = reservationService.createReservation(request);

        // Assert
        assertNotNull(response);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_ThrowsBadRequestWhenCapacityExceeded() {
        // Arrange
        setupSecurityContext();
        ReservationRequest request = new ReservationRequest();
        request.setTableId(1);
        request.setCustomerId(1L);
        request.setNumberOfGuests(5); // Table capacity is 4

        when(tableRepository.findById(1)).thenReturn(Optional.of(mockTable));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> reservationService.createReservation(request));
        assertEquals("Number of guests exceeds table capacity", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void updateReservationStatus_Success() {
        // Arrange
        setupSecurityContext();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(new ReservationResponse());

        // Act
        ReservationResponse response = reservationService.updateReservationStatus(1L, ReservationStatus.da_xac_nhan);

        // Assert
        assertNotNull(response);
        assertEquals(ReservationStatus.da_xac_nhan, mockReservation.getStatus());
        assertNotNull(mockReservation.getConfirmedBy());
        assertNotNull(mockReservation.getConfirmedAt());
        verify(reservationRepository, times(1)).save(mockReservation);
    }
}
