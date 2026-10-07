package com.techvora.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/media")
@RequiredArgsConstructor
public class MediaController {

    private final com.techvora.service.AuditLogService auditLogService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR', 'AUTHOR')")
    public ResponseEntity<?> uploadMedia(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }
        
        try {
            String contentType = file.getContentType() != null ? file.getContentType() : "image/jpeg";
            String base64Image = "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(file.getBytes());
            
            auditLogService.logAction(null, "MEDIA_UPLOAD", file.getOriginalFilename(), "Uploaded media asset: " + file.getOriginalFilename());
            
            return ResponseEntity.ok(Map.of("url", base64Image));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to process image file"));
        }
    }
}
