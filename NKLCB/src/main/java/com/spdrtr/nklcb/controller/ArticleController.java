package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{category_id}")
    public List<Map<String, Object>> GetAllArticlesByCategoryId(@PathVariable Long category_id) {
        return articleService.getAllArticlesByCategoryId(category_id);
    }

    @GetMapping("/all")
    public List<Map<String, Object>> GetAllArticles() {
        List<Article> allArticle = articleService.getAllArticles();
        List<Map<String, Object>> allArticlesDB = new ArrayList<>();
        for(int i=0; i < allArticle.size(); i++){
            Map<String, Object> articleDB = new HashMap<>();

            articleDB.put("title", allArticle.get(i).getTitle());
            articleDB.put("enterprise", allArticle.get(i).getEnterprise());
            articleDB.put("locate", allArticle.get(i).getLocate());
            articleDB.put("reward", allArticle.get(i).getReward());
            articleDB.put("image_uri", allArticle.get(i).getImage_url());
            articleDB.put("official_uri", allArticle.get(i).getOfficial_url());

            allArticlesDB.add(articleDB);
        }

        return allArticlesDB;
    }
}
