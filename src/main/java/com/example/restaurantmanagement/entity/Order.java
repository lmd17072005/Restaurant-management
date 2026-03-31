package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bảng don_hang — sau khi gộp chi_tiet_don_hang vào.
 * Mỗi row = 1 line item (1 món ăn) trong hóa đơn.
 */
@Entity
@Table(name = "don_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_hang_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private Invoice invoice;

    // ---- Fields merged from chi_tiet_don_hang ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "so_luong", nullable = false)
    private Integer quantity;

    @Column(name = "don_gia", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "thanh_tien", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;

    @Column(name = "ghi_chu", length = 500)
    private String note;
    // ------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "trang_thai_don_hang_enum")
    @ColumnTransformer(write = "CAST(? AS trang_thai_don_hang_enum)")
    @Builder.Default
    private OrderStatus status = OrderStatus.cho_che_bien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tao_boi", nullable = false)
    private User createdBy;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
