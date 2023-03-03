package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/articles")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    /*@GetMapping("/{category_id}")
    public List<Map<String, Object>> GetAllArticlesByCategoryId(@PathVariable Long category_id) {
        return articleService.getArticlesByCategoryId(category_id);
    }*/

    /*@GetMapping("/search")
    public List<Map<String, Object>> SearchArticles(
            @RequestParam("type") String type,
            @RequestParam("keyword") String keyword
    ) {
        switch (type){
            case "title":
                return articleService.searchArticlesByTitle(keyword);
            case "enterprise":
                return articleService.searchArticlesByEnterprise(keyword);
        }
        return null;
    }*/

    @GetMapping("/{category_id}")
    public void GetAllArticlesByCategoryId(@PathVariable Long category_id, Model model) {
        model.addAllAttributes(articleService.getArticlesByCategoryId(category_id));
    }

    @GetMapping("/search")
    public void SearchArticles(
            @RequestParam("type") String type,
            @RequestParam("keyword") String keyword,
            Model model
    ) {
        switch (type){
            case "title":
                model.addAllAttributes(articleService.searchArticlesByTitle(keyword));
            case "enterprise":
                model.addAllAttributes(articleService.searchArticlesByEnterprise(keyword));
        }
    }

    /*@GetMapping("/all")
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
    }*/


}
