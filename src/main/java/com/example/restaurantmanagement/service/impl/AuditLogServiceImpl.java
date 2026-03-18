package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.response.AuditLogResponse;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.AuditLogMapper;
import com.example.restaurantmanagement.repository.AuditLogRepository;
import com.example.restaurantmanagement.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAllLogs() {
        return auditLogMapper.toResponseList(auditLogRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse getLogById(Long id) {
        return auditLogMapper.toResponse(auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getLogsByTableAndRecord(String tableName, Long recordId) {
        return auditLogMapper.toResponseList(auditLogRepository.findByTableNameAndRecordId(tableName, recordId));
    }
}

