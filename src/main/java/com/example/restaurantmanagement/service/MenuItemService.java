package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    List<MenuItemResponse> getAllMenuItems();
    MenuItemResponse getMenuItemById(Long id);
    List<MenuItemResponse> getMenuItemsByCategory(Long categoryId);
    List<MenuItemResponse> getAvailableMenuItems();
    List<MenuItemResponse> searchMenuItems(String keyword);
    MenuItemResponse createMenuItem(MenuItemRequest request);
    MenuItemResponse updateMenuItem(Long id, MenuItemRequest request);
    void deleteMenuItem(Long id);
}

