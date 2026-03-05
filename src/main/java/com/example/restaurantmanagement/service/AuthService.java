package com.example.restaurantmanagement.service;
import com.example.restaurantmanagement.dto.request.LoginRequest;
import com.example.restaurantmanagement.dto.request.RegisterRequest;
import com.example.restaurantmanagement.dto.response.AuthResponse;
public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}