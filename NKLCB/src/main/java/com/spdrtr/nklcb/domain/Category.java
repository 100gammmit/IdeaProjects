package com.spdrtr.nklcb.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;
    @Setter @Column(nullable = false, length = 20) private String category_depth1;
    @Setter @Column(length = 50) private String category_depth2;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ArticleCategoryMapping> articleCategoryMappings = new ArrayList<>();
    protected Category(){}

    private Category(String category_depth1, String category_depth2){
        this.category_depth1 = category_depth1;
        this.category_depth2 = category_depth2;
    }

    public static Category of(String category_depth1, String category_depth2){
        return new Category(category_depth1, category_depth2);
    }
}
