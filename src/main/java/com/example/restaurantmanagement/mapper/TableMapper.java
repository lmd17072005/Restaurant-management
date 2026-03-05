package com.example.restaurantmanagement.mapper;
import com.example.restaurantmanagement.dto.request.TableRequest;
import com.example.restaurantmanagement.dto.response.TableResponse;
import com.example.restaurantmanagement.entity.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;
@Mapper(componentModel = "spring")
public interface TableMapper {
    TableResponse toResponse(RestaurantTable table);
    List<TableResponse> toResponseList(List<RestaurantTable> tables);
    RestaurantTable toEntity(TableRequest request);
    void updateEntity(TableRequest request, @MappingTarget RestaurantTable table);
}