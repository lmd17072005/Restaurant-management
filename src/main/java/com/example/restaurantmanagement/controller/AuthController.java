package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.request.LoginRequest;
import com.example.restaurantmanagement.dto.request.RegisterRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.AuthResponse;
import com.example.restaurantmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(authService.register(request)));
    }
    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(request)));
    }
}