package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.IngredientStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "nguyen_lieu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nguyen_lieu_id")
    private Integer id;

    @Column(name = "ten_nguyen_lieu", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "don_vi_tinh", nullable = false, length = 30)
    private String unit;

    @Column(name = "ton_kho", nullable = false, precision = 12, scale = 3)
    @Builder.Default
    private BigDecimal stockQuantity = BigDecimal.ZERO;

    @Column(name = "ton_kho_toi_thieu", nullable = false, precision = 12, scale = 3)
    @Builder.Default
    private BigDecimal minStockQuantity = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "trang_thai_nguyen_lieu_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Builder.Default
    private IngredientStatus status = IngredientStatus.hoat_dong;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
