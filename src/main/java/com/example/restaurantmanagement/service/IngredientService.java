package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.IngredientRequest;
import com.example.restaurantmanagement.dto.response.IngredientResponse;

import java.util.List;

public interface IngredientService {
    List<IngredientResponse> getAllIngredients();
    IngredientResponse getIngredientById(Integer id);
    IngredientResponse createIngredient(IngredientRequest request);
    IngredientResponse updateIngredient(Integer id, IngredientRequest request);
    void deleteIngredient(Integer id);
}

