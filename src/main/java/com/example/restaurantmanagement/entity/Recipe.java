package com.example.restaurantmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cong_thuc_mon", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"mon_id", "nguyen_lieu_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cong_thuc_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguyen_lieu_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "so_luong_can", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantityRequired;
}

