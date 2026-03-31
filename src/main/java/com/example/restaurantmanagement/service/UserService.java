package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.enums.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    List<UserResponse> getUsersByRole(Role role);
    UserResponse updateRole(Long id, Role role);
    void deleteUser(Long id);

    PageResponse<UserResponse> getAllUsers(Pageable pageable);
    PageResponse<UserResponse> getUsersByRole(Role role, Pageable pageable);
    PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable);
}

