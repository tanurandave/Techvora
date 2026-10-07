package com.techvora.repository;

import com.techvora.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, UUID> {
    List<UserProgress> findByUserId(UUID userId);
    Optional<UserProgress> findByUserIdAndRoadmapNodeId(UUID userId, UUID roadmapNodeId);
}
