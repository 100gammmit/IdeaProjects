package com.spdrtr.nklcb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList ="title"),
        @Index(columnList ="enterprise"),
        @Index(columnList ="locate"),
        @Index(columnList = "reward"),
        @Index(columnList ="image_url"),
        @Index(columnList ="official_url")
})
@Entity
public class Article {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @Column(nullable = false) private String originalId;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String enterprise;
    @Column(nullable = false) private String locate;
    @Column(nullable = false) private String reward;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleCategoryMapping> articleCategoryMappings = new ArrayList<>();

    @Column(length = 255) private String image_url;
    @Column(length = 255) private String official_url;

    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<Favorites> favorites = new LinkedHashSet<>();

    protected Article() {}

    @Builder
    public Article(String originalId, String title, String enterprise, String locate, String reward, String image_url, String official_url){
        this.originalId = originalId;
        this.title = title;
        this.enterprise = enterprise;
        this.locate = locate;
        this.reward = reward;
        this.image_url = image_url;
        this.official_url = official_url;
    }

    public void addMappingWithCategory(ArticleCategoryMapping articleCategoryMapping){
        articleCategoryMapping.setArticle(this);
        this.articleCategoryMappings.add(articleCategoryMapping);
    }
}
