package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.IngredientRequest;
import com.example.restaurantmanagement.dto.response.IngredientResponse;
import com.example.restaurantmanagement.entity.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    IngredientResponse toResponse(Ingredient ingredient);
    List<IngredientResponse> toResponseList(List<Ingredient> ingredients);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Ingredient toEntity(IngredientRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(IngredientRequest request, @MappingTarget Ingredient ingredient);
}

