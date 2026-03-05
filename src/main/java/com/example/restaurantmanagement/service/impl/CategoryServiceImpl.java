package com.example.restaurantmanagement.service.impl;
import com.example.restaurantmanagement.dto.request.CategoryRequest;
import com.example.restaurantmanagement.dto.response.CategoryResponse;
import com.example.restaurantmanagement.entity.Category;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.CategoryMapper;
import com.example.restaurantmanagement.repository.CategoryRepository;
import com.example.restaurantmanagement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public List<CategoryResponse> getAllCategories() { return categoryMapper.toResponseList(categoryRepository.findAll()); }
    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toResponse(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id)));
    }
    @Override @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) throw new DuplicateResourceException("Category already exists: " + request.getName());
        return categoryMapper.toResponse(categoryRepository.save(categoryMapper.toEntity(request)));
    }
    @Override @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.findByName(request.getName()).ifPresent(e -> { if (!e.getId().equals(id)) throw new DuplicateResourceException("Category name exists: " + request.getName()); });
        categoryMapper.updateEntity(request, category);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }
    @Override @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category", "id", id);
        categoryRepository.deleteById(id);
    }
}