package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Reservation;
import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByTableId(Integer tableId);

    List<Reservation> findByCustomerId(Long customerId);

    List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.status IN (" +
            "com.example.restaurantmanagement.entity.enums.ReservationStatus.cho_xac_nhan, " +
            "com.example.restaurantmanagement.entity.enums.ReservationStatus.da_xac_nhan) " +
            "AND r.reservationTime >= :startOfDay " +
            "AND r.reservationTime < :endOfDay")
    Long countActiveReservationsToday(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
