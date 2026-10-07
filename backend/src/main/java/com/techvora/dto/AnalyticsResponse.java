package com.techvora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {
    private long totalUsers;
    private long totalArticles;
    private long activeRoadmaps;
    private Map<String, Integer> userGrowthByMonth;
    private Map<String, Integer> pageViewsByMonth;
}
