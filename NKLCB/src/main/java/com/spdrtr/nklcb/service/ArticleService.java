package com.spdrtr.nklcb.service;

import com.beust.ah.A;
import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.dto.ArticleDto;
import com.spdrtr.nklcb.repository.ArticleRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spdrtr.nklcb.service.Crawling.*;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void crawlArticle() throws InterruptedException {
        String url = "https://www.wanted.co.kr/wdlist?country=kr&job_sort=job.popularity_order&years=-1&locations=all";
        process(url);
        List<WebElement> articles = findElements("Card_className__u5rsb");
        String title, enterprise, locate, image_url, official_url;
        int reward;
//        driver.quit();

        for (WebElement article : articles) {
            article.getAttribute("style");
            title = article.findElement(By.className("job-card-position")).getText();
            enterprise = article.findElement(By.className("job-card-company-name")).getText();
            locate = article.findElement(By.className("job-card-company-location")).getText();
            reward = Integer.parseInt(article.findElement(By.className("reward")).getText().replaceAll("[^0-9]", ""));
            image_url = article.findElement(By.cssSelector("a > header")).getAttribute("style");
            image_url = image_url.substring(image_url.indexOf("(")+2, image_url.indexOf(")")-1);
            official_url = article.findElement(By.cssSelector("a")).getAttribute("href");

            ArticleDto dto = ArticleDto.builder()
                    .title(title)
                    .enterprise(enterprise)
                    .locate(locate)
                    .reward(reward)
                    .image_url(image_url)
                    .official_url(official_url)
                    .build();
            saveArticle(dto);

            System.out.println("title:" + title +
                    "\nenterprise:" + enterprise +
                    "\nlocate:" + locate +
                    "\nreward:" + reward +
                    "\nimage_url:" + image_url +
                    "\nofficial_url:" + official_url);
        }
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }
    public Article findArticleById(long ArticleId) {
        return articleRepository.findById(ArticleId).get();
    }

    public List<Article> getAllArticles() {
        List<Article> allArticle = new ArrayList<>();
        long articleSize = articleRepository.findAll().size();

        for(long i = 1; i <= articleSize; i++){
            allArticle.add(findArticleById(i));
        }

        return allArticle;
    }
}
