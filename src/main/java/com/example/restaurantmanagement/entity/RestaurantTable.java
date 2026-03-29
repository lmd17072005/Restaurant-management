package com.example.restaurantmanagement.entity;

import com.example.restaurantmanagement.entity.enums.TableStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "ban_an")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Integer id;

    @Column(name = "ma_ban", nullable = false, unique = true, length = 10)
    private String tableCode;

    @Column(name = "suc_chua", nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, columnDefinition = "trang_thai_ban_enum")
    @ColumnTransformer(write = "?::trang_thai_ban_enum", read = "trang_thai::text")
    @Builder.Default
    private TableStatus status = TableStatus.trong;
}
