package com.spdrtr.nklcb.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ArticleCategoryMapping {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @ManyToOne @JoinColumn(name = "article_id") private Article article;
    @ManyToOne @JoinColumn(name = "category_id") private Category category;

    protected ArticleCategoryMapping() {}
    @Builder
    public ArticleCategoryMapping(Article article, Category category){
        this.article = article;
        this.category = category;
    }
}
