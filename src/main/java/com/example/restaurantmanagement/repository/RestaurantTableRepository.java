package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {
    Optional<RestaurantTable> findByTableCode(String tableCode);
    List<RestaurantTable> findByStatus(TableStatus status);
    boolean existsByTableCode(String tableCode);
}

