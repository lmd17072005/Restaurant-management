package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API")
public class UserController {
    private final UserService userService;
    @GetMapping @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() { return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers())); }
    @GetMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) { return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id))); }
    @PatchMapping("/{id}/role") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Update user role")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(@PathVariable UUID id, @RequestParam Role role) { return ResponseEntity.ok(ApiResponse.success("Role updated", userService.updateRole(id, role))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Delete user")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) { userService.deleteUser(id); return ResponseEntity.ok(ApiResponse.success("User deleted", null)); }
}