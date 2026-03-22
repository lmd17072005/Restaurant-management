package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.InventoryHistoryRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.InventoryHistoryResponse;
import com.example.restaurantmanagement.service.InventoryHistoryService;
import com.example.restaurantmanagement.entity.enums.NotificationMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory-histories")
@RequiredArgsConstructor
@Tag(name = "Inventory History", description = "Inventory history management API")
public class InventoryHistoryController {

    private final InventoryHistoryService inventoryHistoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get all inventory histories")
    public ResponseEntity<ApiResponse<List<InventoryHistoryResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(inventoryHistoryService.getAllHistories()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get inventory history by ID")
    public ResponseEntity<ApiResponse<InventoryHistoryResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(inventoryHistoryService.getHistoryById(id)));
    }

    @GetMapping("/ingredient/{ingredientId}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get histories by ingredient")
    public ResponseEntity<ApiResponse<List<InventoryHistoryResponse>>> getByIngredient(@PathVariable Integer ingredientId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryHistoryService.getHistoriesByIngredient(ingredientId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Create inventory history (nhap/xuat/dieu_chinh)")
    public ResponseEntity<ApiResponse<InventoryHistoryResponse>> create(@Valid @RequestBody InventoryHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(NotificationMessage.INVENTORY_HISTORY_CREATED_SUCCESS, inventoryHistoryService.createHistory(request)));
    }
}

