package com.techvora.service;

import com.techvora.dto.ArticleRequest;
import com.techvora.entity.Article;
import com.techvora.entity.ArticleSlugHistory;
import com.techvora.entity.Category;
import com.techvora.entity.User;
import com.techvora.repository.ArticleRepository;
import com.techvora.repository.ArticleSlugHistoryRepository;
import com.techvora.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleSlugHistoryRepository slugHistoryRepository;
    private final AuditLogService auditLogService;
    private final CategoryRepository categoryRepository;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGESDHASHES = Pattern.compile("(^-|-$)");

    public String generateUniqueSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = EDGESDHASHES.matcher(slug).replaceAll("");
        slug = slug.toLowerCase(Locale.ENGLISH);

        String uniqueSlug = slug;
        int count = 1;
        while (articleRepository.findBySlug(uniqueSlug).isPresent()) {
            uniqueSlug = slug + "-" + count;
            count++;
        }
        return uniqueSlug;
    }

    public Article createArticle(ArticleRequest request, User author) {
        String slug = (request.getSlug() != null && !request.getSlug().isBlank())
                ? generateUniqueSlug(request.getSlug())
                : generateUniqueSlug(request.getTitle());

        com.techvora.entity.ArticleStatus currentStatus = request.getStatus() != null ? request.getStatus() : com.techvora.entity.ArticleStatus.DRAFT;

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(slug)
                .excerpt(request.getExcerpt())
                .content(request.getContent())
                .coverImage(request.getCoverImage())
                .imageAltText(request.getImageAltText())
                .subcategory(request.getSubcategory())
                .autoToc(request.getAutoToc() != null ? request.getAutoToc() : true)
                .tagNames(request.getTags() != null ? request.getTags() : java.util.Collections.emptySet())
                .status(currentStatus)
                .author(author)
                .seoTitle(request.getSeoTitle())
                .seoDescription(request.getSeoDescription())
                .focusKeyword(request.getFocusKeyword())
                .canonicalUrl(request.getCanonicalUrl())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .allowComments(request.getAllowComments() != null ? request.getAllowComments() : true)
                .publishedAt(currentStatus == com.techvora.entity.ArticleStatus.PUBLISHED ? java.time.LocalDateTime.now() : null)
                .build();

        if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
            String catName = request.getCategoryName().trim();
            String catSlug = catName.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]+", "-");
            Category cat = categoryRepository.findByNameIgnoreCase(catName)
                    .or(() -> categoryRepository.findBySlug(catSlug))
                    .orElseGet(() -> categoryRepository.save(Category.builder()
                            .name(catName)
                            .slug(catSlug)
                            .build()));
            article.setCategory(cat);
        } else if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(article::setCategory);
        }

        Article savedArticle = articleRepository.save(article);
        auditLogService.logAction(author, "ARTICLE_CREATE", savedArticle.getId().toString(), "Created article: " + savedArticle.getTitle());
        return savedArticle;
    }

    public Article updateArticle(Article article, ArticleRequest request) {
        String oldTitle = article.getTitle();
        String currentSlug = article.getSlug();

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            article.setTitle(request.getTitle());
        }

        // Determine target slug
        String targetSlug = null;
        if (request.getSlug() != null && !request.getSlug().isBlank() && !request.getSlug().equals(currentSlug)) {
            targetSlug = request.getSlug();
        } else if (request.getTitle() != null && !request.getTitle().equals(oldTitle) && (request.getSlug() == null || request.getSlug().isBlank())) {
            targetSlug = generateUniqueSlug(request.getTitle());
        }

        // Update slug and record history if changed
        if (targetSlug != null && !targetSlug.equals(currentSlug)) {
            if (currentSlug != null && !currentSlug.isBlank()) {
                if (slugHistoryRepository.findByOldSlug(currentSlug).isEmpty()) {
                    ArticleSlugHistory history = ArticleSlugHistory.builder()
                            .article(article)
                            .oldSlug(currentSlug)
                            .build();
                    slugHistoryRepository.save(history);
                }
            }
            article.setSlug(targetSlug);
        }

        // Update other fields
        if (request.getExcerpt() != null) article.setExcerpt(request.getExcerpt());
        if (request.getContent() != null) article.setContent(request.getContent());
        if (request.getCoverImage() != null) article.setCoverImage(request.getCoverImage());
        if (request.getImageAltText() != null) article.setImageAltText(request.getImageAltText());
        if (request.getSubcategory() != null) article.setSubcategory(request.getSubcategory());
        if (request.getAutoToc() != null) article.setAutoToc(request.getAutoToc());
        
        if (request.getTags() != null) {
            if (article.getTagNames() == null) {
                article.setTagNames(new java.util.HashSet<>());
            } else {
                article.getTagNames().clear();
            }
            article.getTagNames().addAll(request.getTags());
        }

        if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
            String catName = request.getCategoryName().trim();
            String catSlug = catName.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]+", "-");
            Category cat = categoryRepository.findByNameIgnoreCase(catName)
                    .or(() -> categoryRepository.findBySlug(catSlug))
                    .orElseGet(() -> categoryRepository.save(Category.builder()
                            .name(catName)
                            .slug(catSlug)
                            .build()));
            article.setCategory(cat);
        } else if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(article::setCategory);
        }

        if (request.getStatus() != null) {
            article.setStatus(request.getStatus());
            if (request.getStatus() == com.techvora.entity.ArticleStatus.PUBLISHED && article.getPublishedAt() == null) {
                article.setPublishedAt(java.time.LocalDateTime.now());
            }
        }
        if (request.getSeoTitle() != null) article.setSeoTitle(request.getSeoTitle());
        if (request.getSeoDescription() != null) article.setSeoDescription(request.getSeoDescription());
        if (request.getFocusKeyword() != null) article.setFocusKeyword(request.getFocusKeyword());
        if (request.getCanonicalUrl() != null) article.setCanonicalUrl(request.getCanonicalUrl());
        if (request.getIsFeatured() != null) article.setIsFeatured(request.getIsFeatured());
        if (request.getAllowComments() != null) article.setAllowComments(request.getAllowComments());

        Article updatedArticle = articleRepository.save(article);
        auditLogService.logAction(article.getAuthor(), "ARTICLE_UPDATE", updatedArticle.getId().toString(), "Updated article: " + updatedArticle.getTitle());
        return updatedArticle;
    }

    public java.util.List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(java.util.UUID id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public void deleteArticle(java.util.UUID id) {
        articleRepository.deleteById(id);
        auditLogService.logAction(null, "ARTICLE_DELETE", id.toString(), "Deleted article with ID: " + id);
    }

    public com.techvora.dto.ArticleResponse mapToResponse(Article article) {
        return com.techvora.dto.ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .slug(article.getSlug())
                .excerpt(article.getExcerpt())
                .content(article.getContent())
                .coverImage(article.getCoverImage())
                .imageAltText(article.getImageAltText())
                .subcategory(article.getSubcategory())
                .autoToc(article.getAutoToc())
                .status(article.getStatus())
                .readingTime(article.getReadingTime())
                .authorName(article.getAuthor() != null ? article.getAuthor().getFirstName() + " " + article.getAuthor().getLastName() : "Techvora Admin")
                .categoryName(article.getCategory() != null ? article.getCategory().getName() : "Spring Boot")
                .tags(article.getTagNames())
                .seoTitle(article.getSeoTitle())
                .seoDescription(article.getSeoDescription())
                .focusKeyword(article.getFocusKeyword())
                .canonicalUrl(article.getCanonicalUrl())
                .isFeatured(article.getIsFeatured())
                .allowComments(article.getAllowComments())
                .publishedAt(article.getPublishedAt())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}
