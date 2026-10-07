package com.techvora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String excerpt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverImage;

    @Column(columnDefinition = "TEXT")
    private String imageAltText;

    private String subcategory;
    private Boolean autoToc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "article_tags",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "article_tag_names", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "tag_name")
    @Builder.Default
    private Set<String> tagNames = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status;

    private Integer readingTime;

    private String seoTitle;

    @Column(columnDefinition = "TEXT")
    private String seoDescription;

    private String focusKeyword;

    @Column(columnDefinition = "TEXT")
    private String canonicalUrl;

    @Builder.Default
    private Boolean isFeatured = false;

    @Builder.Default
    private Boolean allowComments = true;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
