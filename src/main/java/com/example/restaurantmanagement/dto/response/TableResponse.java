package com.example.restaurantmanagement.dto.response;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TableResponse {
    private Long id;
    private Integer tableNumber;
    private Integer capacity;
    private TableStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}