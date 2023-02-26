package com.spdrtr.nklcb.dto;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.Category;
import lombok.Builder;

import java.io.Serializable;

/**
 * A DTO for the {@link com.spdrtr.nklcb.domain.Article} entity
 */
public record ArticleDto(long id, String originalId, String title, String enterprise,
                         String locate, int reward, String image_url,
                         String official_url) implements Serializable {
    @Builder
    public ArticleDto(long id, String originalId, String title, String enterprise,
                      String locate, int reward, String image_url,
                      String official_url){
        this.id = id;
        this.originalId = originalId;
        this.title = title;
        this.enterprise = enterprise;
        this.locate = locate;
        this.reward = reward;
        this.image_url = image_url;
        this.official_url = official_url;
    }

    public Article toEntity(){
        Article article = Article.builder()
                .originalId(originalId)
                .title(title)
                .enterprise(enterprise)
                .locate(locate)
                .reward(reward)
                .image_url(image_url)
                .official_url(official_url)
                .build();
        return article;
    }
}