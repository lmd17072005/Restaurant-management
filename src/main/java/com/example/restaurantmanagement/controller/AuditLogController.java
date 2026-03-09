package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.AuditLogResponse;
import com.example.restaurantmanagement.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Audit log API")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get all audit logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(auditLogService.getAllLogs()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get audit log by ID")
    public ResponseEntity<ApiResponse<AuditLogResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogById(id)));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Get audit logs by table name and record ID")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getByTableAndRecord(
            @RequestParam String tableName, @RequestParam Long recordId) {
        return ResponseEntity.ok(ApiResponse.success(auditLogService.getLogsByTableAndRecord(tableName, recordId)));
    }
}

