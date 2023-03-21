package com.spdrtr.nklcb.dto;

import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import lombok.Builder;

import java.io.Serializable;

import static com.spdrtr.nklcb.dto.ArticleDto.toArticleDto;

/**
 * A DTO for the {@link com.spdrtr.nklcb.domain.ArticleCategoryMapping} entity
 */
public record ArticleCategoryMappingDto(long id, ArticleDto articleDto, CategoryDto categoryDto) implements Serializable {
    @Builder
    public ArticleCategoryMappingDto(long id, ArticleDto articleDto, CategoryDto categoryDto) {
        this.id = id;
        this.articleDto = articleDto;
        this.categoryDto = categoryDto;
    }

    public static ArticleCategoryMappingDto from(ArticleCategoryMapping acm) {
        return new ArticleCategoryMappingDto(
                acm.getId(),
                toArticleDto(acm.getArticle()),
                CategoryDto.from(acm.getCategory())
        );
    }

    public ArticleCategoryMapping toArticleCategoryMappingEntity() {
        return ArticleCategoryMapping.of(articleDto.toEntity(), categoryDto.toEntity());
    }
}