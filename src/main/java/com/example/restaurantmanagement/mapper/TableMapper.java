package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.request.TableRequest;
import com.example.restaurantmanagement.dto.response.TableResponse;
import com.example.restaurantmanagement.entity.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {
    TableResponse toResponse(RestaurantTable table);
    List<TableResponse> toResponseList(List<RestaurantTable> tables);

    @Mapping(target = "id", ignore = true)
    RestaurantTable toEntity(TableRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(TableRequest request, @MappingTarget RestaurantTable table);
}

