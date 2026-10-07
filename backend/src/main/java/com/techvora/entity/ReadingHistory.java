package com.techvora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reading_history")
public class ReadingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    private Integer lastReadPosition;
    private Integer readingProgressPercentage;
    
    private Boolean isCompleted;

    @Column(name = "started_at", updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
        this.lastReadAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastReadAt = LocalDateTime.now();
    }
}
