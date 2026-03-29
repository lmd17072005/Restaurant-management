package com.example.restaurantmanagement.config;

import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.Role;
import com.example.restaurantmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Quản Lý Hệ Thống")
                    .phone("0123456789")
                    .email("admin@restaurant.com")
                    .role(Role.QUAN_LY)
                    .build();
            userRepository.save(admin);
            log.info("Default admin created: admin / admin123");
        }

        if (!userRepository.existsByUsername("nhanvien01")) {
            User staff = User.builder()
                    .username("nhanvien01")
                    .password(passwordEncoder.encode("staff123"))
                    .fullName("Nhân Viên Mẫu")
                    .phone("0987654321")
                    .email("staff@restaurant.com")
                    .role(Role.NHAN_VIEN)
                    .build();
            userRepository.save(staff);
            log.info("Default staff created: nhanvien01 / staff123");
        }

    }
}

