package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.InventoryHistoryResponse;
import com.example.restaurantmanagement.entity.InventoryHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryHistoryMapper {
    @Mapping(source = "ingredient.id", target = "ingredientId")
    @Mapping(source = "ingredient.name", target = "ingredientName")
    @Mapping(source = "performer.id", target = "performerId")
    @Mapping(source = "performer.fullName", target = "performerName")
    InventoryHistoryResponse toResponse(InventoryHistory history);

    List<InventoryHistoryResponse> toResponseList(List<InventoryHistory> histories);
}

