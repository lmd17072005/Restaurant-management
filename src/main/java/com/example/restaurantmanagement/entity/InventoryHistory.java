package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.InventoryTransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_kho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lich_su_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguyen_lieu_id", nullable = false)
    private Ingredient ingredient;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_gd", nullable = false)
    private InventoryTransactionType transactionType;

    @Column(name = "so_luong", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "don_gia", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "thanh_tien", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_thuc_hien", nullable = false)
    private User performer;

    @Column(name = "ngay_giao_dich", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime transactionDate = LocalDateTime.now();
}

