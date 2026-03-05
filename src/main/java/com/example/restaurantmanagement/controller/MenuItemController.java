package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.service.MenuItemService;
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
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Menu item management API")
public class MenuItemController {
    private final MenuItemService menuItemService;
    @GetMapping @Operation(summary = "Get all menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(menuItemService.getAllMenuItems())); }
    @GetMapping("/{id}") @Operation(summary = "Get menu item by ID")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemById(id))); }
    @GetMapping("/category/{categoryId}") @Operation(summary = "Get by category")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getByCategory(@PathVariable Long categoryId) { return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemsByCategory(categoryId))); }
    @GetMapping("/available") @Operation(summary = "Get available items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailable() { return ResponseEntity.ok(ApiResponse.success(menuItemService.getAvailableMenuItems())); }
    @GetMapping("/search") @Operation(summary = "Search menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> search(@RequestParam String keyword) { return ResponseEntity.ok(ApiResponse.success(menuItemService.searchMenuItems(keyword))); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Create menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody MenuItemRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(menuItemService.createMenuItem(request))); }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Update menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) { return ResponseEntity.ok(ApiResponse.success("Updated", menuItemService.updateMenuItem(id, request))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Delete menu item")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { menuItemService.deleteMenuItem(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }
}