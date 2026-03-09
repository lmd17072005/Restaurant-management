package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.TableStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableResponse {
    private Integer id;
    private String tableCode;
    private Integer capacity;
    private TableStatus status;
}

