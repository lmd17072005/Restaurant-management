package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.RecipeResponse;
import com.example.restaurantmanagement.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "menuItemName")
    @Mapping(source = "ingredient.id", target = "ingredientId")
    @Mapping(source = "ingredient.name", target = "ingredientName")
    @Mapping(source = "ingredient.unit", target = "ingredientUnit")
    RecipeResponse toResponse(Recipe recipe);

    List<RecipeResponse> toResponseList(List<Recipe> recipes);
}

