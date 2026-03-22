package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.RecipeRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.RecipeResponse;
import com.example.restaurantmanagement.service.RecipeService;
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
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
@Tag(name = "Recipes", description = "Recipe management API")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get all recipes")
    public ResponseEntity<ApiResponse<List<RecipeResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(recipeService.getAllRecipes()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get recipe by ID")
    public ResponseEntity<ApiResponse<RecipeResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(recipeService.getRecipeById(id)));
    }

    @GetMapping("/menu-item/{menuItemId}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get recipes by menu item")
    public ResponseEntity<ApiResponse<List<RecipeResponse>>> getByMenuItem(@PathVariable Integer menuItemId) {
        return ResponseEntity.ok(ApiResponse.success(recipeService.getRecipesByMenuItem(menuItemId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Create recipe")
    public ResponseEntity<ApiResponse<RecipeResponse>> create(@Valid @RequestBody RecipeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(NotificationMessage.RECIPE_CREATED_SUCCESS, recipeService.createRecipe(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update recipe")
    public ResponseEntity<ApiResponse<RecipeResponse>> update(@PathVariable Long id, @Valid @RequestBody RecipeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.RECIPE_UPDATED_SUCCESS, recipeService.updateRecipe(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Delete recipe")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.ok(ApiResponse.success(NotificationMessage.RECIPE_DELETED_SUCCESS, null));
    }
}

