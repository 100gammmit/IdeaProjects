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
    @Setter @Column(nullable = false, length = 50) private String title;
    @Setter @Column private String condition;
    @Setter @Column(nullable = false, length = 50) private String enterprise;
    @Setter @Column(length = 50) private String deadline;
    @Setter @Column(nullable = false, length = 20) private String locate;
    @Setter @Column(nullable = false, length = 20) private int reward;

    @ManyToOne @JoinColumn(name = "category_id") private Category category;

    @Setter @Column(length = 255) private String image_url;
    @Setter @Column(length = 255) private String official_url;

    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<Favorites> favorites = new LinkedHashSet<>();

    protected Article() {}

    @Builder
    public Article(String title, String enterprise, String locate, int reward, String image_url, String official_url, Category category){
        this.title = title;
        this.enterprise = enterprise;
        this.locate = locate;
        this.reward = reward;
        this.image_url = image_url;
        this.official_url = official_url;
        this.category = category;
    }
}
