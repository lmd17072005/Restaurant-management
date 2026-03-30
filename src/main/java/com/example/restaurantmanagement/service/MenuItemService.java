package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.MenuItemRequest;
import com.example.restaurantmanagement.dto.response.MenuItemResponse;
import com.example.restaurantmanagement.dto.response.PageResponse;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MenuItemService {
    List<MenuItemResponse> getAllMenuItems();
    MenuItemResponse getMenuItemById(Integer id);
    List<MenuItemResponse> getMenuItemsByCategory(Integer categoryId);
    List<MenuItemResponse> searchMenuItems(String keyword);
    MenuItemResponse createMenuItem(MenuItemRequest request);
    MenuItemResponse updateMenuItem(Integer id, MenuItemRequest request);
    void deleteMenuItem(Integer id);

    MenuItemResponse updateMenuItemStatus(Integer id, MenuItemStatus status);
    List<MenuItemResponse> getAvailableMenuItems();
    List<MenuItemResponse> getAvailableMenuItemsByCategory(Integer categoryId);
    List<MenuItemResponse> searchAvailableMenuItems(String keyword);

    PageResponse<MenuItemResponse> getAllMenuItems(Pageable pageable);
    PageResponse<MenuItemResponse> getMenuItemsByCategory(Integer categoryId, Pageable pageable);
    PageResponse<MenuItemResponse> searchMenuItems(String keyword, Pageable pageable);
}

