package com.techvora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmapResponse {
    private UUID id;
    private String title;
    private String slug;
    private String description;
    private List<RoadmapNodeResponse> nodes;
}
