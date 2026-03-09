package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.TableRequest;
import com.example.restaurantmanagement.dto.response.TableResponse;
import com.example.restaurantmanagement.entity.enums.TableStatus;

import java.util.List;

public interface TableService {
    List<TableResponse> getAllTables();
    TableResponse getTableById(Integer id);
    List<TableResponse> getTablesByStatus(TableStatus status);
    TableResponse createTable(TableRequest request);
    TableResponse updateTable(Integer id, TableRequest request);
    TableResponse updateTableStatus(Integer id, TableStatus status);
    void deleteTable(Integer id);
}

