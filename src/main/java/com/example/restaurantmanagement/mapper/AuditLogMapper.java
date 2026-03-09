package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.AuditLogResponse;
import com.example.restaurantmanagement.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    @Mapping(source = "performer.id", target = "performerId")
    @Mapping(source = "performer.fullName", target = "performerName")
    AuditLogResponse toResponse(AuditLog auditLog);

    List<AuditLogResponse> toResponseList(List<AuditLog> auditLogs);
}

