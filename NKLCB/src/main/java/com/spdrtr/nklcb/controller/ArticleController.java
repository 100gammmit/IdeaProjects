package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/crawl")
    public String CrawlAndSave() throws InterruptedException {
        articleService.crawlArticle();
        return "index";
    }

    @GetMapping("/")
    public Map<String, Object> FindArticle() {
        Article article = articleService.findArticleById(1L);
        Map<String, Object> articles = new HashMap<String, Object>();
        articles.put("title", article.getTitle());
        articles.put("enterprise", article.getEnterprise());
        articles.put("locate", article.getLocate());
        articles.put("reward", article.getReward());
        return articles;
    }

    @GetMapping("/all")
    public  List<Map<String, Object>> GetAllArticles() {
        List<Article> allArticle = articleService.getAllArticles();
        List<Map<String, Object>> allArticlesDB = new ArrayList<>();
        for(int i=0; i < allArticle.size(); i++){
            Map<String, Object> articleDB = new HashMap<>();

            articleDB.put("title", allArticle.get(i).getTitle());
            articleDB.put("enterprise", allArticle.get(i).getEnterprise());
            articleDB.put("locate", allArticle.get(i).getLocate());
            articleDB.put("reward", allArticle.get(i).getReward());

            allArticlesDB.add(articleDB);
        }

        return allArticlesDB;
    }
}
