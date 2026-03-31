package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/page")
    @Operation(summary = "Get all menu items with pagination")
    public ResponseEntity<ApiResponse<PageResponse<MenuItemResponse>>> getAllPaged(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAllMenuItems(pageable)));
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available (con_ban) menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailable() {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAvailableMenuItems()));
    }

    @GetMapping("/available/search")
    @Operation(summary = "Search available menu items by keyword")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> searchAvailable(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.searchAvailableMenuItems(keyword)));
    }

    @GetMapping("/available/category/{categoryId}")
    @Operation(summary = "Get available menu items by category")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAvailableByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getAvailableMenuItemsByCategory(categoryId)));
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

    @GetMapping("/category/{categoryId}/page")
    @Operation(summary = "Get by category with pagination")
    public ResponseEntity<ApiResponse<PageResponse<MenuItemResponse>>> getByCategoryPaged(
            @PathVariable Integer categoryId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemsByCategory(categoryId, pageable)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search menu items")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.searchMenuItems(keyword)));
    }

    @GetMapping("/search/page")
    @Operation(summary = "Search menu items with pagination")
    public ResponseEntity<ApiResponse<PageResponse<MenuItemResponse>>> searchPaged(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(menuItemService.searchMenuItems(keyword, pageable)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Create menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(menuItemService.createMenuItem(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> update(@PathVariable Integer id, @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated", menuItemService.updateMenuItem(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Delete menu item")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted", null));
    }
}

