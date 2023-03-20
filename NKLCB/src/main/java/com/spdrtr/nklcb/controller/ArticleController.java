package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("")
    public String MainPage(ModelMap map) {
        return "home";
    }

    /*@GetMapping("/{category_id}")
    public String GetAllArticlesByCategoryId(@PathVariable Long category_id, Pageable pageable, ModelMap map) {
        List<Article> articlePage = articleService.getArticlesByCategoryId(category_id, pageable);

        map.addAttribute("articlePage", articlePage);

        return "home";
    }*/

    // TODO: 페이지네이션 세부내용 처리해야됨
    @GetMapping("/category-selected")
    public String WhenCategorySelected(
            @RequestParam("select_JobGroup") String jobGroup,
            @RequestParam("select_Position") String position,
            @PageableDefault(size = 9) Pageable pageable,
            ModelMap map
    ) {
        Long category_id = Long.parseLong(position);
        try {
            List<Article> articlePage = articleService.getArticlesByCategoryId(category_id);

            map.addAttribute("articlePage", articlePage);
        }catch (Exception e) {
            System.out.println("아무코토"); // TODO: 나중에 지우셈
        }

        return "home";
    }

    @GetMapping("/search")
    public String SearchArticles(
            @RequestParam("type") String type,
            @RequestParam("keyword") String keyword,
            @PageableDefault(size = 9) Pageable pageable,
            ModelMap map
    ) {
        switch (type){
            case "title":
                map.addAttribute("articlePage", articleService.searchArticlesByTitle(keyword, pageable));
                break;
            case "enterprise":
                map.addAttribute("articlePage",articleService.searchArticlesByEnterprise(keyword, pageable));
                break;
        }

        return "home";
    }
}
