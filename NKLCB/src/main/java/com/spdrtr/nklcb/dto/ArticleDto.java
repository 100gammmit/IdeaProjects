package com.spdrtr.nklcb.dto;

import com.spdrtr.nklcb.domain.Article;
import lombok.Builder;

import java.io.Serializable;

/**
 * A DTO for the {@link com.spdrtr.nklcb.domain.Article} entity
 */
public record ArticleDto(long id, String title, String condition, String enterprise, String deadline,
                         String locate, String reward,
                         String official_url) implements Serializable {
    @Builder
    public ArticleDto(long id, String title, String condition, String enterprise, String deadline,
                      String locate, String reward,
                      String official_url){
        this.id = id;
        this.title = title;
        this.condition = condition;
        this.enterprise = enterprise;
        this.deadline = deadline;
        this.locate = locate;
        this.reward = reward;
        this.official_url = official_url;
    }

    public Article toEntity(){
        Article article = Article.builder()
                .title(title)
                .enterprise(enterprise)
                .locate(locate)
                .reward(reward)
                .build();
        return article;
    }
}