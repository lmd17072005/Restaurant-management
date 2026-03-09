package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mon_an")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mon_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_id", nullable = false)
    private Category category;

    @Column(name = "ten_mon", nullable = false, length = 150)
    private String name;

    @Column(name = "gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "hinh_anh", length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    @Builder.Default
    private MenuItemStatus status = MenuItemStatus.con_ban;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}

