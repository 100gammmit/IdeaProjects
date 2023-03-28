package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CrawlingController {
    private final ArticleService articleService;

    @GetMapping("/crawl")
    public String CrawlAndSave() throws InterruptedException {
        articleService.crawlAllArticleWithCategory();
        return "index";
    }
}
