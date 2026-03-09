package com.example.restaurantmanagement.dto.response;

import com.example.restaurantmanagement.entity.enums.AuditAction;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {
    private Long id;
    private String tableName;
    private Long recordId;
    private AuditAction action;
    private String oldValues;
    private String newValues;
    private Long performerId;
    private String performerName;
    private LocalDateTime timestamp;
    private String ipAddress;
}

