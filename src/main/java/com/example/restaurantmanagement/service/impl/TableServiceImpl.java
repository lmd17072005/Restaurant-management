package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.TableRequest;
import com.example.restaurantmanagement.dto.response.TableResponse;
import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.DuplicateResourceException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.TableMapper;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
import com.example.restaurantmanagement.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final RestaurantTableRepository tableRepository;
    private final TableMapper tableMapper;

    @Override
    public List<TableResponse> getAllTables() {
        return tableMapper.toResponseList(tableRepository.findAll());
    }

    @Override
    public TableResponse getTableById(Integer id) {
        return tableMapper.toResponse(tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", id)));
    }

    @Override
    public List<TableResponse> getTablesByStatus(TableStatus status) {
        return tableMapper.toResponseList(tableRepository.findByStatus(status));
    }

    @Override
    @Transactional
    public TableResponse createTable(TableRequest request) {
        if (tableRepository.existsByTableCode(request.getTableCode())) {
            throw new DuplicateResourceException("Table code exists: " + request.getTableCode());
        }
        RestaurantTable table = tableMapper.toEntity(request);
        if (request.getStatus() == null) {
            table.setStatus(TableStatus.trong);
        }
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public TableResponse updateTable(Integer id, TableRequest request) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", id));
        tableRepository.findByTableCode(request.getTableCode()).ifPresent(e -> {
            if (!e.getId().equals(id)) {
                throw new DuplicateResourceException("Table code exists: " + request.getTableCode());
            }
        });
        tableMapper.updateEntity(request, table);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public TableResponse updateTableStatus(Integer id, TableStatus status) {
        RestaurantTable table = tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", id));
        table.setStatus(status);
        return tableMapper.toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public void deleteTable(Integer id) {
        if (!tableRepository.existsById(id)) {
            throw new ResourceNotFoundException("Table", "id", id);
        }
        tableRepository.deleteById(id);
    }
}

