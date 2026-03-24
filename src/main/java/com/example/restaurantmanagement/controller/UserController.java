package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.service.UserService;
import com.example.restaurantmanagement.entity.enums.NotificationMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get users by role")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUsersByRole(role)));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Update user role")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(@PathVariable Long id, @RequestParam Role role) {
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.USER_ROLE_UPDATED_SUCCESS, userService.updateRole(id, role)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Delete user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.USER_DELETED_SUCCESS, null));
    }
}

