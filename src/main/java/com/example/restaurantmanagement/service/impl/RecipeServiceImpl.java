package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.RecipeRequest;
import com.example.restaurantmanagement.dto.response.RecipeResponse;
import com.example.restaurantmanagement.entity.Ingredient;
import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.Recipe;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.RecipeMapper;
import com.example.restaurantmanagement.repository.IngredientRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.RecipeRepository;
import com.example.restaurantmanagement.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponse> getAllRecipes() {
        return recipeMapper.toResponseList(recipeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeResponse getRecipeById(Long id) {
        return recipeMapper.toResponse(recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecipeResponse> getRecipesByMenuItem(Integer menuItemId) {
        return recipeMapper.toResponseList(recipeRepository.findByMenuItemId(menuItemId));
    }

    @Override
    @Transactional
    public RecipeResponse createRecipe(RecipeRequest request) {
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", request.getMenuItemId()));
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", request.getIngredientId()));
        if (recipeRepository.existsByMenuItemIdAndIngredientId(request.getMenuItemId(), request.getIngredientId())) {
            throw new DuplicateResourceException("Recipe already exists for this menu item and ingredient");
        }
        Recipe recipe = Recipe.builder()
                .menuItem(menuItem)
                .ingredient(ingredient)
                .quantityRequired(request.getQuantityRequired())
                .build();
        return recipeMapper.toResponse(recipeRepository.save(recipe));
    }

    @Override
    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", id));
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", request.getMenuItemId()));
        Ingredient ingredient = ingredientRepository.findById(request.getIngredientId())
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", request.getIngredientId()));
        recipeRepository.findByMenuItemIdAndIngredientId(request.getMenuItemId(), request.getIngredientId())
                .ifPresent(e -> {
                    if (!e.getId().equals(id)) {
                        throw new DuplicateResourceException("Recipe already exists for this menu item and ingredient");
                    }
                });
        recipe.setMenuItem(menuItem);
        recipe.setIngredient(ingredient);
        recipe.setQuantityRequired(request.getQuantityRequired());
        return recipeMapper.toResponse(recipeRepository.save(recipe));
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recipe", "id", id);
        }
        recipeRepository.deleteById(id);
    }
}

