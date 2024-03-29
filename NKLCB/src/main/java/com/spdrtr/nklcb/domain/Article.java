package com.spdrtr.nklcb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
public class Article extends AuditingFields{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @Column(nullable = false) private String originalId;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String enterprise;
    @Column(nullable = false) private String locate;
    @Column(nullable = false) private String reward;
    @Column(nullable = false, columnDefinition = "integer default 0") private int view_count;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleCategoryMapping> articleCategoryMappings = new ArrayList<>();

    @Column(length = 255) private String image_url;
    @Column(length = 255) private String official_url;  // TODO: original_id만을 통해 상세 게시글 페이지로 넘어가는 방식으로 변경하였으므로, 삭제 고려
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
