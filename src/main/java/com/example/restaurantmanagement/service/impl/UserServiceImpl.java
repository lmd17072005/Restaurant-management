package com.example.restaurantmanagement.service.impl;
import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.UserMapper;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public List<UserResponse> getAllUsers() { return userMapper.toResponseList(userRepository.findAll()); }
    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toResponse(user);
    }
    @Override @Transactional
    public UserResponse updateRole(UUID id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }
    @Override @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("User", "id", id);
        userRepository.deleteById(id);
    }
}