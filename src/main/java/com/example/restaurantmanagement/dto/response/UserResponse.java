package com.example.restaurantmanagement.dto.response;
import com.example.restaurantmanagement.entity.enums.Role;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
}