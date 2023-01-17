package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/category")
@RestController
public class CategoryController {
    private CategoryService sc;

    @GetMapping ("/")
    public String CrawlAndSave() throws InterruptedException {
        sc.saveCategory();
        return "index";
    }


}
