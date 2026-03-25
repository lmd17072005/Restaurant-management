package com.example.restaurantmanagement;

import com.example.restaurantmanagement.dto.request.LoginRequest;
import com.example.restaurantmanagement.dto.request.RegisterRequest;
import com.example.restaurantmanagement.dto.response.AuthResponse;
import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.mapper.UserMapper;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.security.JwtUtil;
import com.example.restaurantmanagement.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;



    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setName("New User");
        request.setEmail("test@gmail.com");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> authService.register(request));
        assertTrue(exception.getMessage().contains("Email đã tồn tại"));
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenDataIsValid() {
        // GIVEN: Phân nhánh 3 - Dữ liệu hoàn toàn hợp lệ
        RegisterRequest request = new RegisterRequest();
        request.setName("New User");
        request.setPassword("123456");
        request.setEmail("new@gmail.com");

        User savedUser = new User();
        savedUser.setUsername("new@gmail.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(any(User.class))).thenReturn("mock-jwt-token");

        when(userMapper.toResponse(any(User.class))).thenReturn(new UserResponse());

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());

        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("123456");

        User mockUser = new User();
        mockUser.setUsername("admin@gmail.com");
        mockUser.setPassword("hashedPassword");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("123456", "hashedPassword")).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtUtil.generateToken(mockUser)).thenReturn("mock-jwt-token");
        when(userMapper.toResponse(mockUser)).thenReturn(new UserResponse());

        // WHEN
        AuthResponse response = authService.login(request);

        // THEN
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getAccessToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }
}