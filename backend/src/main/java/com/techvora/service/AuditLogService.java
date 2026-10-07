package com.techvora.service;

import com.techvora.entity.AuditLog;
import com.techvora.entity.User;
import com.techvora.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLog logAction(User user, String action, String entityId, String details) {
        try {
            AuditLog log = AuditLog.builder()
                    .user(user)
                    .action(action)
                    .entityId(entityId)
                    .details(details)
                    .build();
            return auditLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Failed to save audit log: " + e.getMessage());
            return null;
        }
    }
}
