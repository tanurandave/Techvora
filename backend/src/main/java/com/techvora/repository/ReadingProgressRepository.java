package com.techvora.repository;

import com.techvora.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, UUID> {
    Optional<ReadingProgress> findByUserIdAndArticleId(UUID userId, UUID articleId);
}
