package com.techvora.repository;

import com.techvora.entity.ArticleSlugHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleSlugHistoryRepository extends JpaRepository<ArticleSlugHistory, UUID> {
    Optional<ArticleSlugHistory> findByOldSlug(String oldSlug);
}
