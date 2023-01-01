package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.Crawling;
import com.spdrtr.nklcb.service.SaveCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
    private Crawling cr;

    @Autowired
    public ArticleController(Crawling cr) {
        this.cr = cr;
    }
    @GetMapping("/")
    public String CrawlAndSave() throws InterruptedException {
        cr.saveArticle();
        return "index";
    }
}
