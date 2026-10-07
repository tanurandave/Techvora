package com.techvora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {
    private UUID id;
    private String username;
    private String action;
    private String entityId;
    private String details;
    private LocalDateTime createdAt;
}
