package com.techvora.controller;

import com.techvora.dto.ArticleResponse;
import com.techvora.entity.Article;
import com.techvora.entity.Bookmark;
import com.techvora.entity.User;
import com.techvora.repository.ArticleRepository;
import com.techvora.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkRepository bookmarkRepository;
    private final ArticleRepository articleRepository;

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> addBookmark(@PathVariable UUID articleId, @AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) return ResponseEntity.notFound().build();

        if (bookmarkRepository.findByUserIdAndArticleId(user.getId(), articleId).isEmpty()) {
            Bookmark bookmark = Bookmark.builder()
                    .user(user)
                    .article(article)
                    .build();
            bookmarkRepository.save(bookmark);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable UUID articleId, @AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        
        bookmarkRepository.findByUserIdAndArticleId(user.getId(), articleId)
                .ifPresent(bookmarkRepository::delete);
                
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getBookmarks(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        
        List<ArticleResponse> responses = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(bookmark -> {
                    Article article = bookmark.getArticle();
                    return ArticleResponse.builder()
                            .id(article.getId())
                            .title(article.getTitle())
                            .slug(article.getSlug())
                            .excerpt(article.getExcerpt())
                            .coverImage(article.getCoverImage())
                            .readingTime(article.getReadingTime())
                            .build();
                })
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(responses);
    }
}
