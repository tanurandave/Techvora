package com.techvora.repository;

import com.techvora.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findBySlug(String slug);
    Page<Article> findByStatus(com.techvora.entity.ArticleStatus status, Pageable pageable);
    Page<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
