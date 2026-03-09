package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemResponse {
    private Integer id;
    private Integer categoryId;
    private String categoryName;
    private String name;
    private BigDecimal price;
    private String imageUrl;
    private MenuItemStatus status;
    private LocalDateTime createdAt;
}

