package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.OrderItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "chi_tiet_don_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chi_tiet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "don_hang_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "so_luong", nullable = false)
    private Integer quantity;

    @Column(name = "don_gia", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "thanh_tien", precision = 12, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotal;

    @Column(name = "ghi_chu", length = 500)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    @Builder.Default
    private OrderItemStatus status = OrderItemStatus.cho_che_bien;
}

