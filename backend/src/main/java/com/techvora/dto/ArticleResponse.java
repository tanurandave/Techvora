package com.techvora.dto;

import com.techvora.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse {
    private UUID id;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private String coverImage;
    private String imageAltText;
    private String subcategory;
    private Boolean autoToc;
    private ArticleStatus status;
    private Integer readingTime;
    private String authorName;
    private String categoryName;
    private Set<String> tags;
    private String seoTitle;
    private String seoDescription;
    private String focusKeyword;
    private String canonicalUrl;
    private Boolean isFeatured;
    private Boolean allowComments;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
