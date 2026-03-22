package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.service.MenuItemService;
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
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Menu item management API")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping //da get duoc
    @Operation(summary = "Get all menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAllMenuItems()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemById(id)));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get by category")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemsByCategory(categoryId)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> search(@RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.searchMenuItems(keyword)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Create menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(NotificationMessage.MENU_ITEM_CREATED_SUCCESS, menuItemService.createMenuItem(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(@PathVariable Integer id, @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.MENU_ITEM_UPDATED_SUCCESS, menuItemService.updateMenuItem(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Delete menu item")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.MENU_ITEM_DELETED_SUCCESS, null));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update menu item status (het_mon, ngung_ban, con_ban)")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateStatus(
            @PathVariable Integer id,
            @RequestParam MenuItemStatus status) {
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.MENU_ITEM_STATUS_UPDATED_SUCCESS, menuItemService.updateMenuItemStatus(id, status)));
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available menu items (con_ban only)")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailable() {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAvailableMenuItems()));
    }

    @GetMapping("/available/category/{categoryId}")
    @Operation(summary = "Get available menu items by category")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailableByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAvailableMenuItemsByCategory(categoryId)));
    }

    @GetMapping("/available/search")   //da fix xong dung sua them
    @Operation(summary = "Search available menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> searchAvailable(@RequestParam(defaultValue = "") String keyword) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.searchAvailableMenuItems(keyword)));
    }
}

