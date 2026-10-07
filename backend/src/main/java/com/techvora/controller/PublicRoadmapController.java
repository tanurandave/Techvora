package com.techvora.controller;

import com.techvora.dto.RoadmapNodeResponse;
import com.techvora.dto.RoadmapResponse;
import com.techvora.entity.Roadmap;
import com.techvora.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roadmaps")
@RequiredArgsConstructor
public class PublicRoadmapController {

    private final RoadmapRepository roadmapRepository;

    @GetMapping
    public ResponseEntity<List<RoadmapResponse>> getAllRoadmaps() {
        List<RoadmapResponse> responses = roadmapRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<RoadmapResponse> getRoadmapBySlug(@PathVariable String slug) {
        return roadmapRepository.findBySlug(slug)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private RoadmapResponse mapToResponse(Roadmap roadmap) {
        return RoadmapResponse.builder()
                .id(roadmap.getId())
                .title(roadmap.getTitle())
                .slug(roadmap.getSlug())
                .description(roadmap.getDescription())
                .nodes(roadmap.getNodes().stream().map(node -> RoadmapNodeResponse.builder()
                        .id(node.getId())
                        .title(node.getTitle())
                        .description(node.getDescription())
                        .orderIndex(node.getOrderIndex())
                        .linkedArticleSlug(node.getLinkedArticle() != null ? node.getLinkedArticle().getSlug() : null)
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
