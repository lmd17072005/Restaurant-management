package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.RecipeRequest;
import com.example.restaurantmanagement.dto.response.RecipeResponse;

import java.util.List;

public interface RecipeService {
    List<RecipeResponse> getAllRecipes();
    RecipeResponse getRecipeById(Long id);
    List<RecipeResponse> getRecipesByMenuItem(Integer menuItemId);
    RecipeResponse createRecipe(RecipeRequest request);
    RecipeResponse updateRecipe(Long id, RecipeRequest request);
    void deleteRecipe(Long id);
}

