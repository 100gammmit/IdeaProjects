package com.spdrtr.nklcb.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class ArticleCategoryMapping {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    @Setter @ManyToOne @JoinColumn(name = "article_id") Article article;
    @ManyToOne @JoinColumn(name = "category_id") Category category;

    public void takeCategory(Category category){
        this.category = category;
        category.getArticleCategoryMappings().add(this);
    }

    public ArticleCategoryMapping() {}
    private ArticleCategoryMapping(Article article, Category category) {
        this.article = article;
        this.category = category;
    }

    public static ArticleCategoryMapping of(Article article, Category category) {
        return new ArticleCategoryMapping(article, category);
    }
}
