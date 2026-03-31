package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.IngredientRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.IngredientResponse;
import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.service.IngredientService;
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
@RequestMapping("/api/v1/ingredients")
@RequiredArgsConstructor
@Tag(name = "Ingredients", description = "Ingredient management API")
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get all ingredients with pagination")
    public ResponseEntity<ApiResponse<PageResponse<IngredientResponse>>> getAllPaged(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(ingredientService.getAllIngredients(pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get ingredient by ID")
    public ResponseEntity<ApiResponse<IngredientResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success(ingredientService.getIngredientById(id)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Search ingredients by name with pagination")
    public ResponseEntity<ApiResponse<PageResponse<IngredientResponse>>> searchIngredients(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success(ingredientService.searchIngredients(keyword, pageable)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Create ingredient")
    public ResponseEntity<ApiResponse<IngredientResponse>> create(@Valid @RequestBody IngredientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(ingredientService.createIngredient(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update ingredient")
    public ResponseEntity<ApiResponse<IngredientResponse>> update(@PathVariable Integer id, @Valid @RequestBody IngredientRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Updated", ingredientService.updateIngredient(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Delete ingredient")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted", null));
    }
}

