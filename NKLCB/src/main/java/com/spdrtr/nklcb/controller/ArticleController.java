package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.SaveArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
    private SaveArticle cr;

    @Autowired
    public ArticleController(SaveArticle cr) {
        this.cr = cr;
    }
    @GetMapping("/")
    public String CrawlAndSave() throws InterruptedException {
        cr.saveArticle();
        return "index";
    }
}
