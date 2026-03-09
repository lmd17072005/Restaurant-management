package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.CategoryStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Integer id;
    private String name;
    private CategoryStatus status;
}

