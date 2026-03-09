package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.entity.Category;
import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.MenuItemMapper;
import com.example.restaurantmanagement.repository.CategoryRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemMapper menuItemMapper;

    @Override
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemMapper.toResponseList(menuItemRepository.findAll());
    }

    @Override
    public MenuItemResponse getMenuItemById(Integer id) {
        return menuItemMapper.toResponse(menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id)));
    }

    @Override
    public List<MenuItemResponse> getMenuItemsByCategory(Integer categoryId) {
        return menuItemMapper.toResponseList(menuItemRepository.findByCategoryId(categoryId));
    }

    @Override
    public List<MenuItemResponse> searchMenuItems(String keyword) {
        return menuItemMapper.toResponseList(menuItemRepository.findByNameContainingIgnoreCase(keyword));
    }

    @Override
    @Transactional
    public MenuItemResponse createMenuItem(MenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        MenuItem menuItem = menuItemMapper.toEntity(request);
        menuItem.setCategory(category);
        if (request.getStatus() == null) {
            menuItem.setStatus(MenuItemStatus.con_ban);
        }
        return menuItemMapper.toResponse(menuItemRepository.save(menuItem));
    }

    @Override
    @Transactional
    public MenuItemResponse updateMenuItem(Integer id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getCategoryId()));
        menuItemMapper.updateEntity(request, menuItem);
        menuItem.setCategory(category);
        return menuItemMapper.toResponse(menuItemRepository.save(menuItem));
    }

    @Override
    @Transactional
    public void deleteMenuItem(Integer id) {
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem", "id", id);
        }
        menuItemRepository.deleteById(id);
    }
}

