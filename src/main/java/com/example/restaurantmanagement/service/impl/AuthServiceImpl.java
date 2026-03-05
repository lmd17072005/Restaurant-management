package com.example.restaurantmanagement.service.impl;
import com.example.restaurantmanagement.dto.request.LoginRequest;
import com.example.restaurantmanagement.dto.request.RegisterRequest;
import com.example.restaurantmanagement.dto.response.AuthResponse;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
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
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .build();
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);
        return AuthResponse.builder().accessToken(token).tokenType("Bearer").user(userMapper.toResponse(savedUser)).build();
    }
    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);
        return AuthResponse.builder().accessToken(token).tokenType("Bearer").user(userMapper.toResponse(user)).build();
    }
}