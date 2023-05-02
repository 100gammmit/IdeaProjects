package com.spdrtr.nklcb.controller;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("")
    public String MainPage(
            @PageableDefault(size = 6, sort = "view_count", direction = Sort.Direction.DESC)
            Pageable pageable,
            ModelMap map) {
        List<Article> popularDevelope = articleService.getArticlesByJobGroup("개발", pageable);
        map.addAttribute("articlePage", popularDevelope);
        return "home";
    }

    /*@GetMapping("/{category_id}")
    public String GetAllArticlesByCategoryId(@PathVariable Long category_id, Pageable pageable, ModelMap map) {
        List<Article> articlePage = articleService.getArticlesByCategoryId(category_id, pageable);

        map.addAttribute("articlePage", articlePage);

        return "home";
    }*/

    @GetMapping("/category-selected")
    public String WhenCategorySelected(
            @RequestParam("select_JobGroup") String jobGroup,
            @RequestParam("select_Position") String position,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(defaultValue = "0") Integer page,
            // @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Pageable pageable = null;

        System.out.println("sort = " + sort);
        if(sort.equals("popular")) {
            pageable = PageRequest.of(page, 9, Sort.by("view_count").descending());
            System.out.println(pageable.getSort().toString());
        }
        else {
            pageable = PageRequest.of(page, 9, Sort.by("createdAt").descending());
            System.out.println(pageable.getSort().toString());
        }

        try {
            Page<Article> articlePage = articleService.getArticlesByCategoryPosition(position, pageable);
            //페이지블럭 처리
            //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
            int nowPage = articlePage.getPageable().getPageNumber() + 1;
            //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
            int startPage =  Math.max(nowPage - 4, 1);
            int endPage = Math.min(nowPage+4, articlePage.getTotalPages());
            long articleCount = articlePage.getTotalElements();

            map.addAttribute("articleCount", articleCount);
            map.addAttribute("articlePage", articlePage);
            map.addAttribute("jobGroup", jobGroup);
            map.addAttribute("position", position);
            map.addAttribute("nowPage",nowPage);
            map.addAttribute("startPage", startPage);
            map.addAttribute("endPage", endPage);
            map.addAttribute("sort", sort);
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

        int nowPage = articlePage.getPageable().getPageNumber() + 1;
        int startPage =  Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage+4, articlePage.getTotalPages());
        long articleCount = articlePage.getTotalElements();

        map.addAttribute("articleCount", articleCount); // TODO: 조회 결과 article개수 표시하는 부분 view에서 구현 필요(모든 컨트롤러별)
        map.addAttribute("type", type);
        map.addAttribute("keyword", keyword);
        map.addAttribute("nowPage",nowPage);
        map.addAttribute("startPage", startPage);
        map.addAttribute("endPage", endPage);

        return "search";
    }

    @GetMapping("detail/{originalId}")
    public String ViewDetail(
            @PathVariable String originalId){
        articleService.updateViewCount(originalId);
        return "redirect:https://www.wanted.co.kr/wd/" + originalId;
    }
}
