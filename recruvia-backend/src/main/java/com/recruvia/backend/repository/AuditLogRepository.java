package com.recruvia.backend.repository;

import com.recruvia.backend.entity.AuditLog;
import com.recruvia.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByUser(User user);

    List<AuditLog> findByEntityName(String entityName);

}