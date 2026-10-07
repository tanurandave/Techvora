package com.techvora.controller;

import com.techvora.dto.AnalyticsResponse;
import com.techvora.repository.ArticleRepository;
import com.techvora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnalyticsResponse> getDashboardAnalytics() {
        // In a real application, these maps would be populated by grouping queries
        // from the database. For MVP demonstration of the dashboard, we return mocked aggregated data.
        
        AnalyticsResponse response = AnalyticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalArticles(articleRepository.count())
                .activeRoadmaps(15) // Mocked
                .userGrowthByMonth(Map.of(
                        "Jan", 120,
                        "Feb", 250,
                        "Mar", 400,
                        "Apr", 750,
                        "May", 1100
                ))
                .pageViewsByMonth(Map.of(
                        "Jan", 5000,
                        "Feb", 12000,
                        "Mar", 28000,
                        "Apr", 45000,
                        "May", 89000
                ))
                .build();
                
        return ResponseEntity.ok(response);
    }
}
