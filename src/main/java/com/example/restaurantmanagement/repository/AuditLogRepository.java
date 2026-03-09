package com.example.restaurantmanagement.repository;

import com.example.restaurantmanagement.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTableNameAndRecordId(String tableName, Long recordId);
    List<AuditLog> findByPerformerId(Long userId);
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}

