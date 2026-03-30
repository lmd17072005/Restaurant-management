package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Ingredient;
import com.example.restaurantmanagement.entity.enums.IngredientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Optional<Ingredient> findByName(String name);
    boolean existsByName(String name);
    List<Ingredient> findByStatus(IngredientStatus status);

    Page<Ingredient> findByStatus(IngredientStatus status, Pageable pageable);
    Page<Ingredient> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

