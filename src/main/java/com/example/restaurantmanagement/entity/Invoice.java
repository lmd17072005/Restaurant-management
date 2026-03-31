package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hoa_don_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_id", nullable = false)
    private RestaurantTable table;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private User customer;

    @Column(name = "ma_hoa_don", nullable = false, unique = true, length = 20)
    private String invoiceCode;

    @Column(name = "tam_tinh", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "giam_gia", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "ly_do_giam_gia", length = 255)
    private String discountReason;

    @Column(name = "tong_tien", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "trang_thai_hoa_don_enum")
    @ColumnTransformer(write = "CAST(? AS trang_thai_hoa_don_enum)")
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.chua_thanh_toan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_boi", nullable = false)
    private User openedBy;

    @Column(name = "ngay_tao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "ngay_dong")
    private LocalDateTime closedAt;

    @Version
    @Column(name = "row_version", nullable = false)
    @Builder.Default
    private Integer rowVersion = 1;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();
}

