package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.InventoryHistory;
import com.example.restaurantmanagement.entity.enums.InventoryTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Long> {
    List<InventoryHistory> findByIngredientId(Integer ingredientId);
    List<InventoryHistory> findByTransactionType(InventoryTransactionType type);
    List<InventoryHistory> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}

