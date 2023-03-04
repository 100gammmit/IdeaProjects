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

}
