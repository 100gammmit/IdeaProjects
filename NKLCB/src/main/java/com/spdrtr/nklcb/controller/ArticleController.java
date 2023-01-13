package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.ArticleService;
import com.spdrtr.nklcb.service.SaveArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/articles")
@RestController
public class ArticleController {
    private final SaveArticle saveArticle;
    private final ArticleService articleService;

    @GetMapping("/")
    public String CrawlAndSave() throws InterruptedException {
        saveArticle.saveArticle();
        return "index";
    }

}
