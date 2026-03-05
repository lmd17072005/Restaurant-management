package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.request.CategoryRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.CategoryResponse;
import com.example.restaurantmanagement.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management API")
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping @Operation(summary = "Get all categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(categoryService.getAllCategories())); }
    @GetMapping("/{id}") @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryById(id))); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Create category")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(categoryService.createCategory(request))); }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Update category")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) { return ResponseEntity.ok(ApiResponse.success("Updated", categoryService.updateCategory(id, request))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Delete category")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { categoryService.deleteCategory(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }
}