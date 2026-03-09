package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Category;
import com.example.restaurantmanagement.entity.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    List<Category> findByStatus(CategoryStatus status);
}

