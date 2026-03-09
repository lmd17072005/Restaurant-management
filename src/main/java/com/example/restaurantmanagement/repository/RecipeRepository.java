package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByMenuItemId(Integer menuItemId);
    List<Recipe> findByIngredientId(Integer ingredientId);
    Optional<Recipe> findByMenuItemIdAndIngredientId(Integer menuItemId, Integer ingredientId);
    boolean existsByMenuItemIdAndIngredientId(Integer menuItemId, Integer ingredientId);
}

