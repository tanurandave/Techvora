package com.techvora.controller;

import com.techvora.dto.ArticleResponse;
import com.techvora.entity.Article;
import com.techvora.entity.ArticleStatus;
import com.techvora.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class PublicBlogController {

    private final ArticleRepository articleRepository;
    private final com.techvora.service.ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> getPublishedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Article> articles = articleRepository.findByStatus(ArticleStatus.PUBLISHED, pageable);
        
        Page<ArticleResponse> response = articles.map(articleService::mapToResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleResponse> getArticleBySlug(@PathVariable String slug) {
        return articleRepository.findBySlug(slug)
                .map(articleService::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
