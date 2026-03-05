package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.CategoryRequest;
import com.example.restaurantmanagement.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}

