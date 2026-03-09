package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.InventoryHistoryRequest;
import com.example.restaurantmanagement.dto.response.InventoryHistoryResponse;
import com.example.restaurantmanagement.entity.Ingredient;
import com.example.restaurantmanagement.entity.InventoryHistory;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.InventoryHistoryMapper;
import com.example.restaurantmanagement.repository.IngredientRepository;
import com.example.restaurantmanagement.repository.InventoryHistoryRepository;
import com.example.restaurantmanagement.service.InventoryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryHistoryServiceImpl implements InventoryHistoryService {

    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final IngredientRepository ingredientRepository;
    private final InventoryHistoryMapper inventoryHistoryMapper;

    @Override
    public List<InventoryHistoryResponse> getAllHistories() {
        return inventoryHistoryMapper.toResponseList(inventoryHistoryRepository.findAll());
    }

    @Override
    public InventoryHistoryResponse getHistoryById(Long id) {
        return inventoryHistoryMapper.toResponse(inventoryHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryHistory", "id", id)));
    }

    @Override
    public List<InventoryHistoryResponse> getHistoriesByIngredient(Integer ingredientId) {
        return inventoryHistoryMapper.toResponseList(inventoryHistoryRepository.findByIngredientId(ingredientId));
    }

    @Override
    @Transactional
    public InventoryHistoryResponse createHistory(InventoryHistoryRequest request) {
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", request.getIngredientId()));

        User currentUser = getCurrentUser();

        InventoryHistory history = InventoryHistory.builder()
                .ingredient(ingredient)
                .transactionType(request.getTransactionType())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .performer(currentUser)
                .build();

        // Stock update is handled by PostgreSQL trigger fn_lich_su_kho_cap_nhat_ton
        return inventoryHistoryMapper.toResponse(inventoryHistoryRepository.save(history));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
}

