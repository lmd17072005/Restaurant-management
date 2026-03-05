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
        if (!userRepository.existsByEmail("admin@restaurant.com")) {
            User admin = User.builder().email("admin@restaurant.com").password(passwordEncoder.encode("admin123")).fullName("System Admin").phone("0123456789").role(Role.ADMIN).build();
            userRepository.save(admin);
            log.info("Default admin created: admin@restaurant.com / admin123");
        }
        if (!userRepository.existsByEmail("staff@restaurant.com")) {
            User staff = User.builder().email("staff@restaurant.com").password(passwordEncoder.encode("staff123")).fullName("Default Staff").phone("0987654321").role(Role.STAFF).build();
            userRepository.save(staff);
            log.info("Default staff created: staff@restaurant.com / staff123");
        }
    }
}