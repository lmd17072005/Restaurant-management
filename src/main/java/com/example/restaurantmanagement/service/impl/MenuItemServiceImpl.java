package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.entity.Category;
import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.MenuItemMapper;
import com.example.restaurantmanagement.repository.CategoryRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAllMenuItems() {
        return menuItemMapper.toResponseList(menuItemRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(Integer id) {
        return menuItemMapper.toResponse(menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByCategory(Integer categoryId) {
        return menuItemMapper.toResponseList(menuItemRepository.findByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional
    public MenuItemResponse updateMenuItemStatus(Integer id, MenuItemStatus status) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
        menuItem.setStatus(status);
        return menuItemMapper.toResponse(menuItemRepository.save(menuItem));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAvailableMenuItems() {
        return menuItemMapper.toResponseList(menuItemRepository.findByStatus(MenuItemStatus.con_ban));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getAvailableMenuItemsByCategory(Integer categoryId) {
        return menuItemMapper.toResponseList(
                menuItemRepository.findByStatusAndCategoryId(MenuItemStatus.con_ban, categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> searchAvailableMenuItems(String keyword) {
        return menuItemMapper.toResponseList(
                menuItemRepository.findByNameContainingIgnoreCaseAndStatus(keyword, MenuItemStatus.con_ban));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MenuItemResponse> getAllMenuItems(Pageable pageable) {
        Page<MenuItem> page = menuItemRepository.findAll(pageable);
        List<MenuItemResponse> content = menuItemMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MenuItemResponse> getMenuItemsByCategory(Integer categoryId, Pageable pageable) {
        Page<MenuItem> page = menuItemRepository.findByCategoryId(categoryId, pageable);
        List<MenuItemResponse> content = menuItemMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MenuItemResponse> searchMenuItems(String keyword, Pageable pageable) {
        Page<MenuItem> page = menuItemRepository.findByNameContainingIgnoreCase(keyword, pageable);
        List<MenuItemResponse> content = menuItemMapper.toResponseList(page.getContent());
        return PageResponse.of(page, content);
    }
}

