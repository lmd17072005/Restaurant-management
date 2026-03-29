package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;

@Entity
@Table(name = "dat_ban")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dat_ban_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id", nullable = false)
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private User customer;

    @Column(name = "so_nguoi", nullable = false)
    private Integer numberOfGuests;

    @Column(name = "thoi_gian_dat", nullable = false)
    private LocalDateTime reservationTime;

    @Column(name = "ghi_chu", length = 500)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "trang_thai_dat_ban_enum")
    @ColumnTransformer(
            write = "?::trang_thai_dat_ban_enum",
            read = "trang_thai::text"
    )
    @Builder.Default
    private ReservationStatus status = ReservationStatus.cho_xac_nhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tao_boi", nullable = false)
    private User createdBy;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xac_nhan_boi")
    private User confirmedBy;

    @Column(name = "ngay_xac_nhan")
    private LocalDateTime confirmedAt;
}

