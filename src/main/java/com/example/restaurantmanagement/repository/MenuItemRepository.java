package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByCategoryId(Integer categoryId);
    List<MenuItem> findByStatus(MenuItemStatus status);
    List<MenuItem> findByNameContainingIgnoreCase(String name);
}

