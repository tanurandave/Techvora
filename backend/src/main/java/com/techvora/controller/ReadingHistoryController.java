package com.techvora.controller;

import com.techvora.entity.Article;
import com.techvora.entity.ReadingProgress;
import com.techvora.entity.User;
import com.techvora.repository.ArticleRepository;
import com.techvora.repository.ReadingProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class ReadingHistoryController {

    private final ReadingProgressRepository progressRepository;
    private final ArticleRepository articleRepository;

    @PostMapping("/{articleId}")
    public ResponseEntity<Void> updateProgress(
            @PathVariable UUID articleId,
            @RequestParam int percentage,
            @AuthenticationPrincipal User user) {
            
        if (user == null) return ResponseEntity.status(401).build();
        
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) return ResponseEntity.notFound().build();

        ReadingProgress progress = progressRepository.findByUserIdAndArticleId(user.getId(), articleId)
                .orElse(ReadingProgress.builder()
                        .user(user)
                        .article(article)
                        .build());
                        
        progress.setProgressPercentage(percentage);
        progress.setLastReadAt(LocalDateTime.now());
        
        if (percentage >= 95) {
            progress.setIsCompleted(true);
        }
        
        progressRepository.save(progress);
        return ResponseEntity.ok().build();
    }
}
