package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
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
    public String GetAllArticlesByCategoryId(@PathVariable Long category_id, Pageable pageable, ModelMap map) {
        List<Article> articlePage = articleService.getArticlesByCategoryId(category_id, pageable);

        map.addAttribute("articlePage", articlePage);

        return "home";
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
