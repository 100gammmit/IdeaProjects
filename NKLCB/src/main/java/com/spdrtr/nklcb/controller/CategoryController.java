package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.Crawling;
import com.spdrtr.nklcb.service.SaveCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryController {
    private SaveCategory sc;

    @Autowired
    public CategoryController(SaveCategory sc) {
        this.sc = sc;
    }
    @GetMapping ("/cate")
    public String CrawlAndSave() throws InterruptedException {
        sc.saveCategory();
        return "index";
    }
}
