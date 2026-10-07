package com.techvora.controller;

import com.techvora.dto.ArticleResponse;
import com.techvora.entity.ArticleStatus;
import com.techvora.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final ArticleRepository articleRepository;

    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> searchArticles(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // MVP Search implementation using simple LIKE queries
        // In a real production scenario, this should use PostgreSQL Full-Text Search (tsvector) or OpenSearch.
        Page<com.techvora.entity.Article> results = articleRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                        q, q, PageRequest.of(page, size));
                        
        // For MVP, filter out unpublished articles manually or create a specific query
        // Normally we would just use findBy...AndStatus in the repository.

                        
        Page<ArticleResponse> response = results.map(article -> ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .excerpt(article.getExcerpt())
                .coverImage(article.getCoverImage())
                .readingTime(article.getReadingTime())
                .build());
                
        return ResponseEntity.ok(response);
    }
}
