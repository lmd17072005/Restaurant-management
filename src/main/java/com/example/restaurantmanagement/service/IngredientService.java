package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.IngredientRequest;
import com.example.restaurantmanagement.dto.response.IngredientResponse;
import com.example.restaurantmanagement.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IngredientService {
    List<IngredientResponse> getAllIngredients();
    IngredientResponse getIngredientById(Integer id);
    IngredientResponse createIngredient(IngredientRequest request);
    IngredientResponse updateIngredient(Integer id, IngredientRequest request);
    void deleteIngredient(Integer id);

    PageResponse<IngredientResponse> getAllIngredients(Pageable pageable);
    PageResponse<IngredientResponse> searchIngredients(String keyword, Pageable pageable);
}

