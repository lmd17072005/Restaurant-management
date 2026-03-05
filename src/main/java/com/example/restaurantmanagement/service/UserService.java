package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.enums.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID id);
    UserResponse updateRole(UUID id, Role role);
    void deleteUser(UUID id);
}

