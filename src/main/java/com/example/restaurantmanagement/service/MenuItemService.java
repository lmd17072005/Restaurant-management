package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    List<MenuItemResponse> getAllMenuItems();
    MenuItemResponse getMenuItemById(Integer id);
    List<MenuItemResponse> getMenuItemsByCategory(Integer categoryId);
    List<MenuItemResponse> searchMenuItems(String keyword);
    MenuItemResponse createMenuItem(MenuItemRequest request);
    MenuItemResponse updateMenuItem(Integer id, MenuItemRequest request);
    void deleteMenuItem(Integer id);
}

