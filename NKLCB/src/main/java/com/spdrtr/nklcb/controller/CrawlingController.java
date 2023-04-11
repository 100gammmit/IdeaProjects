package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.spdrtr.nklcb.service.Scheduling.getNowDateTime24;

@RequiredArgsConstructor
@RestController
public class CrawlingController {
    private final ArticleService articleService;

    @GetMapping("/crawl")
    public String CrawlAndSave() throws InterruptedException {
        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ArticleService] : [crawlAllArticleWithCategory]");
        System.out.println("[Started] : " + getNowDateTime24());
        System.out.println("=======================================");
        System.out.println("\n");

        articleService.crawlAllArticleWithCategory();

        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ArticleService] : [crawlAllArticleWithCategory]");
        System.out.println("[Ended] : " + getNowDateTime24());
        System.out.println("=======================================");
        System.out.println("\n");

        return "index";
    }
}
