package com.example.restaurantmanagement.mapper;
import com.example.restaurantmanagement.dto.request.CategoryRequest;
import com.example.restaurantmanagement.dto.response.CategoryResponse;
import com.example.restaurantmanagement.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    List<CategoryResponse> toResponseList(List<Category> categories);
    Category toEntity(CategoryRequest request);
    void updateEntity(CategoryRequest request, @MappingTarget Category category);
}