package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public String FindArticle(Model model) {
        Article article = articleService.findArticleById(1L);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("enterprise", article.getEnterprise());
        model.addAttribute("locate", article.getLocate());
        model.addAttribute("reward", article.getReward());
        return "";
    }
}
