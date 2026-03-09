package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.enums.Role;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    List<UserResponse> getUsersByRole(Role role);
    UserResponse updateRole(Long id, Role role);
    void deleteUser(Long id);
}

