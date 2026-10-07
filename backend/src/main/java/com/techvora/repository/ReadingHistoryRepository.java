package com.techvora.repository;

import com.techvora.entity.ReadingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingHistoryRepository extends JpaRepository<ReadingHistory, UUID> {
    List<ReadingHistory> findByUserId(UUID userId);
    Optional<ReadingHistory> findByUserIdAndArticleId(UUID userId, UUID articleId);
}
