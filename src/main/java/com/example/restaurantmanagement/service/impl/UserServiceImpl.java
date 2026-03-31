package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.CreateStaffRequest;
import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.UserMapper;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getUsersByRole(Role role) {
        return userMapper.toResponseList(userRepository.findByRole(role));
    }

    @Override
    @Transactional
    public UserResponse updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserResponse createStaff(CreateStaffRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists!");
        }
        
        Role role = request.getRole();
        if (role == null || role == Role.KHACH_HANG) {
            role = Role.NHAN_VIEN;
        }
        
        User user = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(role)
                .build();
        
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        List<UserResponse> content = userMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsersByRole(Role role, Pageable pageable) {
        Page<User> page = userRepository.findByRole(role, pageable);
        List<UserResponse> content = userMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> searchUsers(String keyword, Pageable pageable) {
        Page<User> page = userRepository.findByFullNameContainingIgnoreCase(keyword, pageable);
        List<UserResponse> content = userMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }
}

