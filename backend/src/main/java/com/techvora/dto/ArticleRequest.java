package com.techvora.dto;

import com.techvora.entity.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private String coverImage;
    private String imageAltText;
    private String subcategory;
    private Boolean autoToc;
    private UUID categoryId;
    private String categoryName;
    private Set<UUID> tagIds;
    private Set<String> tags;
    private ArticleStatus status;
    private String seoTitle;
    private String seoDescription;
    private String focusKeyword;
    private String canonicalUrl;
    private Boolean isFeatured;
    private Boolean allowComments;
}
