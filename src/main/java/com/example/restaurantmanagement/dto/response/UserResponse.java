package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.entity.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private Role role;
    private String fullName;
    private String phone;
    private String email;
    private String username;
    private UserStatus status;
    private LocalDateTime createdAt;
}

