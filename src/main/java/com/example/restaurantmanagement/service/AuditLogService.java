package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.response.AuditLogResponse;

import java.util.List;

public interface AuditLogService {
    List<AuditLogResponse> getAllLogs();
    AuditLogResponse getLogById(Long id);
    List<AuditLogResponse> getLogsByTableAndRecord(String tableName, Long recordId);
}

