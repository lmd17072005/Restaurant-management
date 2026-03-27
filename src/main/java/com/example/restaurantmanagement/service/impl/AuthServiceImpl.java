package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.LoginRequest;
import com.example.restaurantmanagement.dto.request.RegisterRequest;
import com.example.restaurantmanagement.dto.response.AuthResponse;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.mapper.UserMapper;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.security.JwtUtil;
import com.example.restaurantmanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() == null || !request.getEmail().endsWith("@gmail.com")) {
            throw new BadRequestException("Couldn't find available email , please use Google Gmail");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("This Email already exists ,please login again or use another email!");
        }
        User user = User.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getName())
                .email(request.getEmail())
                .role(Role.KHACH_HANG)
                .build();
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userMapper.toResponse(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("This email does not exists, please check your email or create one!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Wrong password!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword()));
                
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(userMapper.toResponse(user))
                .build();
    }
}

