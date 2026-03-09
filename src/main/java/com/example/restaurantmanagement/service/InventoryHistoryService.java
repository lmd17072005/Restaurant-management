package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.InventoryHistoryRequest;
import com.example.restaurantmanagement.dto.response.InventoryHistoryResponse;

import java.util.List;

public interface InventoryHistoryService {
    List<InventoryHistoryResponse> getAllHistories();
    InventoryHistoryResponse getHistoryById(Long id);
    List<InventoryHistoryResponse> getHistoriesByIngredient(Integer ingredientId);
    InventoryHistoryResponse createHistory(InventoryHistoryRequest request);
}

