package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByCategoryId(Integer categoryId);

    List<MenuItem> findByStatus(MenuItemStatus status);

    List<MenuItem> findByStatusAndCategoryId(MenuItemStatus status, Integer categoryId);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByNameContainingIgnoreCaseAndStatus(String name, MenuItemStatus status);

    Page<MenuItem> findByCategoryId(Integer categoryId, Pageable pageable);
    Page<MenuItem> findByStatus(MenuItemStatus status, Pageable pageable);
    Page<MenuItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
