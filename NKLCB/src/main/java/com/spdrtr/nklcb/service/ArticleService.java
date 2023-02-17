package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import com.spdrtr.nklcb.domain.Category;
import com.spdrtr.nklcb.dto.ArticleDto;
import com.spdrtr.nklcb.repository.ArticleCategoryMappingRepository;
import com.spdrtr.nklcb.repository.ArticleRepository;
import com.spdrtr.nklcb.repository.CategoryRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spdrtr.nklcb.service.Crawling.*;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleCategoryMappingRepository articleCategoryMappingRepository;
    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          CategoryRepository categoryRepository,
                          ArticleCategoryMappingRepository articleCategoryMappingRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.articleCategoryMappingRepository = articleCategoryMappingRepository;
    }

    public void crawlArticleWithCategory() throws InterruptedException {
        String url = "https://www.wanted.co.kr/wdlist?country=kr&job_sort=job.latest_order&years=-1&locations=all";
        process(url);
        logIn();
        Thread.sleep(5000);

        String original_id, title, enterprise, locate, image_url, official_url, category_depth1, category_depth2;
        int reward;

        findElement("JobGroup_JobGroup__H1m1m").click();
        List<WebElement> Big_categories = findElements("JobGroupItem_JobGroupItem__xXzAi");
        WebElement Big_category = Big_categories.get(0);
        category_depth1 = Big_category.getText();
        Big_category.click();

        findElement("JobCategory_JobCategory__btn__k3EFe").click();

        for(int i=1; i <= 6; i++){
            List<WebElement> Small_categories =  findElements("JobCategoryItem_JobCategoryItem__oUaZr");
            WebElement Small_category = Small_categories.get(i);
            category_depth2 = Small_category.getText();
            Small_categories.get(i-1).click();
            Small_category.click();
            // 소분류 카테고리 선택완료 버튼 클릭
            findElement("Button_Button__root__V1ie3").click();

            Category categoryEntity = Category.of(category_depth1, category_depth2);
            categoryRepository.save(categoryEntity);

        /*for(int s=0; s < 3; s++){
            scrollBottom();
            Thread.sleep(1000);
        }*/
            Thread.sleep(1500);
            List<WebElement> articles = findElements("Card_className__u5rsb");

            for (WebElement article : articles) {
                try{
                    WebElement meta = article.findElement(By.cssSelector("a"));
                    original_id = meta.getAttribute("data-position-id");    // TODO: original_id별로 구분해서 중복시 article에 추가 안하는 부분 구현해야 함
                    title = article.findElement(By.className("job-card-position")).getText();
                    enterprise = article.findElement(By.className("job-card-company-name")).getText();
                    locate = article.findElement(By.className("job-card-company-location")).getText();
                    reward = Integer.parseInt(article.findElement(By.className("reward")).getText().replaceAll("[^0-9]", ""));
                    image_url = article.findElement(By.cssSelector("a > header")).getAttribute("style");
                    image_url = image_url.substring(image_url.indexOf("(")+2, image_url.indexOf(")")-1);
                    official_url = meta.getAttribute("href");
                } catch (Exception e){
                    continue;
                }

                ArticleDto articleDto = ArticleDto.builder()
                        .original_id(original_id)
                        .title(title)
                        .enterprise(enterprise)
                        .locate(locate)
                        .reward(reward)
                        .image_url(image_url)
                        .official_url(official_url)
                        .build();

                Article articleEntity = articleDto.toEntity();
                articleRepository.save(articleEntity);

                System.out.println("\noriginal_id:" + original_id +
                        "\ntitle:" + title +
                        "\nenterprise:" + enterprise +
                        "\nlocate:" + locate +
                        "\nreward:" + reward +
                        "\nimage_url:" + image_url +
                        "\nofficial_url:" + official_url);

                ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();
                articleEntity.addMappingWithCategory(articleCategoryMapping);
                articleCategoryMapping.takeCategory(categoryEntity);
                articleCategoryMappingRepository.save(articleCategoryMapping);
            }
            Thread.sleep(1000);
            scrollTop();
            findElement("JobCategory_JobCategory__btn__k3EFe").click();
        }
    }
    public Article findArticleById(long article_id) {
        return articleRepository.findById(article_id).get();
    }

    public List<Article> findAllArticleByCategoryId(long category_id){
        List<Article> articles = new ArrayList<>();
        for(ArticleCategoryMapping articleCategoryMapping : articleCategoryMappingRepository.findAllByCategoryId(category_id)){
            articles.add(articleCategoryMapping.getArticle());
        }
        return articles;
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
