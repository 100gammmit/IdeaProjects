package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        map.addAttribute("articlePage", articleService.getArticlesInMain());
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
            @PageableDefault(page = 0, size = 9, sort = "id") Pageable pageable,
            ModelMap map
    ) {
        Long category_id = Long.parseLong(position);    // position이 String타입으로 넘어오기 때문에 id타입인 Long타입으로 파싱

        try {
            Page<Article> articlePage = articleService.getArticlesByCategoryId(category_id, pageable);
            //페이지블럭 처리
            //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
            int nowPage = articlePage.getPageable().getPageNumber() + 1;
            //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
            int startPage =  Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage+9, articlePage.getTotalPages());

            map.addAttribute("articlePage", articlePage);
            map.addAttribute("jobGroup", jobGroup);
            map.addAttribute("position", position);
            map.addAttribute("nowPage",nowPage);
            map.addAttribute("startPage", startPage);
            map.addAttribute("endPage", endPage);
        }catch (Exception e) {
            System.out.println("아무코토"); // TODO: 나중에 지우셈
        }

        return "category_selected";
    }

    @GetMapping("/search")
    public String SearchArticles(
            @RequestParam("type") String type,
            @RequestParam("keyword") String keyword,
            @PageableDefault(page = 0, size = 9, sort = "id") Pageable pageable,
            ModelMap map
    ) {
        Page<Article> articlePage = null;
        switch (type){
            case "title":
                articlePage = articleService.searchArticlesByTitle(keyword, pageable);
                map.addAttribute("articlePage", articlePage);
                break;
            case "enterprise":
                articlePage = articleService.searchArticlesByEnterprise(keyword, pageable);
                map.addAttribute("articlePage", articlePage);
                break;
        }
        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = articlePage.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+9, articlePage.getTotalPages());

        map.addAttribute("type", type);
        map.addAttribute("keyword", keyword);
        map.addAttribute("nowPage",nowPage);
        map.addAttribute("startPage", startPage);
        map.addAttribute("endPage", endPage);

        return "search";
    }
}
