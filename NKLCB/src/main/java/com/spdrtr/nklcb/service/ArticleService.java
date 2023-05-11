package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import com.spdrtr.nklcb.domain.Category;
import com.spdrtr.nklcb.dto.ArticleDto;
import com.spdrtr.nklcb.repository.ArticleCategoryMappingRepository;
import com.spdrtr.nklcb.repository.ArticleRepository;
import com.spdrtr.nklcb.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spdrtr.nklcb.service.Crawling.*;
import static com.spdrtr.nklcb.service.Scheduling.getNowDateTime24;

@Service
@Transactional
@RequiredArgsConstructor
@EnableScheduling
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleCategoryMappingRepository articleCategoryMappingRepository;

    public void crawlAllArticleWithCategory() throws InterruptedException {
        String url = "https://www.wanted.co.kr/wdlist?country=kr&job_sort=company.response_rate_order&years=-1&locations=all";

        process(url);
        logIn();
        Thread.sleep(5000);

        String original_id, title, enterprise, locate, reward, image_url, official_url, category_depth1, category_depth2;

        findElement("JobGroup_JobGroup__H1m1m").click();    // 대분류 카테고리 목록 열기

        WebElement Big_category = findElements("JobGroupItem_JobGroupItem__xXzAi").get(18);

        // 대분류 카테고리 인덱스 8 이상일시 활성화
        scrollTo(Big_category);
        Thread.sleep(500);
        scrollTop();


        category_depth1 = Big_category.getText();

        Big_category.click();   //대분류 카테고리 버튼 클릭

        findElement("JobCategory_JobCategory__btn__k3EFe").click();     // 소분류 카테고리 목록 열기

        int Small_category_size =  findElements("JobCategoryItem_JobCategoryItem__oUaZr").size();
        for(int i=1; i < Small_category_size; i++) {
            List<WebElement> Small_categories =  findElements("JobCategoryItem_JobCategoryItem__oUaZr");
            WebElement Small_category = Small_categories.get(i);
            category_depth2 = Small_category.getText();
            Small_categories.get(i-1).click();      // 이전에 크롤링한 소분류카테고리 선택 비활성화
            Small_category.click();     // 크롤링할 소분류 카테고리 활성화

            findElement("Button_Button__root__V1ie3").click();      // 소분류 카테고리 선택완료 버튼 클릭

            Category categoryEntity = Category.of(category_depth1, category_depth2);
            categoryRepository.save(categoryEntity);

            for(int s=0; s < 20; s++){
                scrollBottom();
                Thread.sleep(1000);
            }


            List<WebElement> articles = new ArrayList<>();
            int reCnt = 0;
            while(articles.isEmpty()){
                Thread.sleep(1500);
                try {
                    articles = findElements("Card_className__u5rsb");
                } catch (Exception e) {
                    System.out.println("e = " + e);
                }
                if(reCnt >= 5) break;
                reCnt++;
            }

            if(articles.isEmpty()) {
                Thread.sleep(1000);
                scrollTop();
                Thread.sleep(1500);
                findElement("JobCategory_JobCategory__btn__k3EFe").click();
                continue;
            }

            Thread.sleep(1500);
            for (WebElement article : articles) {
                WebElement meta = article.findElement(By.cssSelector("a")); // job_card의 id, position, 상세 페이지 링크 등 메타 정보 수용된 코드부분

                original_id = meta.getAttribute("data-position-id");  // 원티드에서 관리하는 원래 게시글 id를 통해서 기존에 있던 article인지 검사하기 위하여 우선 추출

                ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();

                Optional<Article> Pre_article = articleRepository.findByOriginalId(original_id);
                // 이미 저장되어있는 article이 있는 경우 category만 추가하고 새로운 도메인을 저장하면 안됨
                // 이를 필터링하는 조건문
                if(Pre_article.isEmpty()) {
                    title = article.findElement(By.className("job-card-position")).getText();
                    enterprise = article.findElement(By.className("job-card-company-name")).getText();
                    locate = article.findElement(By.className("job-card-company-location")).getText();
                    reward = article.findElement(By.className("reward")).getText();
                    image_url = article.findElement(By.cssSelector("a > header")).getAttribute("style");
                    image_url = image_url.substring(image_url.indexOf("(")+2, image_url.indexOf(")")-1);
                    official_url = meta.getAttribute("href");

                    ArticleDto articleDto = ArticleDto.builder()
                            .originalId(original_id)
                            .title(title)
                            .enterprise(enterprise)
                            .locate(locate)
                            .reward(reward)
                            .image_url(image_url)
                            .official_url(official_url)
                            .build();

                    Article articleEntity = articleDto.toEntity();
                    articleRepository.save(articleEntity);
                    articleEntity.addMappingWithCategory(articleCategoryMapping);

                    System.out.println(
                            "\n=====================================================" +
                                    "\nINSERT ARTICLE :" +
                                    "\noriginal_id:" + original_id +
                                    "\ntitle:" + title +
                                    "\nenterprise:" + enterprise +
                                    "\nlocate:" + locate +
                                    "\nreward:" + reward +
                                    "\nimage_url:" + image_url +
                                    "\nofficial_url:" + official_url +
                                    "\n=====================================================");
                }
                else {
                    Pre_article.get().addMappingWithCategory(articleCategoryMapping);

                    System.out.println("이미 존재하는 article \nPre_article.get().getArticleCategoryMappings() = " + Pre_article.get().getArticleCategoryMappings());
                }

                articleCategoryMapping.takeCategory(categoryEntity);
                articleCategoryMappingRepository.save(articleCategoryMapping);
            }
            Thread.sleep(1000);
            scrollTop();
            Thread.sleep(1500);
            findElement("JobCategory_JobCategory__btn__k3EFe").click();
        }
        quitDriver();
    }

    public void updateNewArticle() throws InterruptedException {
        String url = "https://www.wanted.co.kr/wdlist?country=kr&job_sort=company.response_rate_order&years=-1&locations=all";
        process(url);
        logIn();
        Thread.sleep(5000);

        String original_id, title, enterprise, locate, reward, image_url, official_url, category;

        findElement("JobGroup_JobGroup__H1m1m").click();
        List<WebElement> Big_categories = findElements("JobGroupItem_JobGroupItem__xXzAi");
        WebElement Big_category = Big_categories.get(0);

        //대분류 카테고리 버튼 클릭
        Big_category.click();

        findElement("JobCategory_JobCategory__btn__k3EFe").click();

        for(int i=1; i <= 3; i++){
            List<WebElement> Small_categories =  findElements("JobCategoryItem_JobCategoryItem__oUaZr");
            WebElement Small_category = Small_categories.get(i);
            category = Small_category.getText();
            Small_categories.get(i-1).click();
            Small_category.click();
            // 소분류 카테고리 선택완료 버튼 클릭
            findElement("Button_Button__root__V1ie3").click();

            Category categoryEntity = categoryRepository.findByCategoryPosition(category);

        for(int s=0; s < 3; s++){
            scrollBottom();
            Thread.sleep(1000);
        }

            List<WebElement> articles = new ArrayList<>();
            while(articles.isEmpty()){
                Thread.sleep(1500);
                try {
                    articles = findElements("Card_className__u5rsb");
                } catch (NoSuchElementException nSE){
                    System.out.println("nSE = " + nSE);
                } catch (ElementClickInterceptedException eCIE){
                    System.out.println("eCIE = " + eCIE);
                }
            }

            for (WebElement article : articles) {
                WebElement meta = article.findElement(By.cssSelector("a")); // job_card의 id, position, 상세 페이지 링크 등 메타 정보 수용된 코드부분
                original_id = meta.getAttribute("data-position-id");  // 원티드에서 관리하는 원래 게시글 id를 통해서 기존에 있던 article인지 검사하기 위하여 우선 추출

                ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();

                Optional<Article> Pre_article = articleRepository.findByOriginalId(original_id);
                // 이미 저장되어있는 article이 있는 경우 새로운 도메인을 저장하면 안됨
                // 이를 필터링하는 조건문
                if(Pre_article.isEmpty()) {
                    title = article.findElement(By.className("job-card-position")).getText();
                    enterprise = article.findElement(By.className("job-card-company-name")).getText();
                    locate = article.findElement(By.className("job-card-company-location")).getText();
                    reward = article.findElement(By.className("reward")).getText();
                    image_url = article.findElement(By.cssSelector("a > header")).getAttribute("style");
                    image_url = image_url.substring(image_url.indexOf("(")+2, image_url.indexOf(")")-1);
                    official_url = meta.getAttribute("href");

                    ArticleDto articleDto = ArticleDto.builder()
                            .originalId(original_id)
                            .title(title)
                            .enterprise(enterprise)
                            .locate(locate)
                            .reward(reward)
                            .image_url(image_url)
                            .official_url(official_url)
                            .build();

                    Article articleEntity = articleDto.toEntity();
                    articleRepository.save(articleEntity);
                    articleEntity.addMappingWithCategory(articleCategoryMapping);
                    articleCategoryMapping.takeCategory(categoryEntity);
                    articleCategoryMappingRepository.save(articleCategoryMapping);

                    System.out.println(
                            "\n=====================================================" +
                                    "\nINSERT NEW ARTICLE :" +
                                    "\noriginal_id:" + original_id +
                                    "\ntitle:" + title +
                                    "\nenterprise:" + enterprise +
                                    "\nlocate:" + locate +
                                    "\nreward:" + reward +
                                    "\nimage_url:" + image_url +
                                    "\nofficial_url:" + official_url +
                                    "\n=====================================================");
                }
            }
            Thread.sleep(1000);
            scrollTop();
            Thread.sleep(2000);
            findElement("JobCategory_JobCategory__btn__k3EFe").click();
        }
        quitDriver();
    }

    public Article findArticleById(long article_id) {
        return articleRepository.findById(article_id).get();
    }

    /**
     * main home에서 8개의 랜덤한 article데이터를 출력하기 위한 매서드
     * @return List<Article>
     */

    /**
     * List타입을 파라미터로 받고
     * Controller에서 JSON타입으로 데이터 전달을 하기 위해 타입 반환
     * @param allArticle
     * @return {name : data}
     * TODO: 유기당한 흔적 삭제 고려
     */
    /*public List<Map<String, Object>> getAllArticleDBByArticleList(List<Article> allArticle) {
        List<Map<String, Object>> allArticlesDB = new ArrayList<>();
        for(int i=0; i < allArticle.size(); i++){
            Map<String, Object> articleDB = new HashMap<>();

            articleDB.put("title", allArticle.get(i).getTitle());
            articleDB.put("enterprise", allArticle.get(i).getEnterprise());
            articleDB.put("locate", allArticle.get(i).getLocate());
            articleDB.put("reward", allArticle.get(i).getReward());
            articleDB.put("image_uri", allArticle.get(i).getImage_url());
            articleDB.put("official_uri", allArticle.get(i).getOfficial_url());

            allArticlesDB.add(articleDB);
        }
        return allArticlesDB;
    }*/

    /**
     * categoryId를 받아 해당하는 카테고리를 가진 모든 article을 반환
     * @param categoryId
     * @return {name : data}
     * TODO: 카테고리 id가 아닌 depth2로 받아오는 방식으로 변경되었으므로 마지막에 삭제 고려
     */
    public Page<Article> getArticlesByCategoryId(Long categoryId, Pageable pageable) {
        Page<Article> allArticle = articleRepository.findArticlesByCategoryId(categoryId, pageable);

        return allArticle;
    }

    /**
     * category_position(category_depth2) 이름을 받아 해당하는 카테고리를 가진 모든 article을 반환
     * @param position
     * @return Page<Article>
     */
    public Page<Article> getArticlesByCategoryPosition(String position, Pageable pageable) {
        Page<Article> Articles = articleRepository.findArticlesByCategoryPosition(position, pageable);

        return Articles;
    }

    /**
     * category_JobGroup(category_depth1) 이름을 받아 해당하는 카테고리를 가진 모든 article을 반환
     * @param jobgroup
     * @return List<Article>
     */
    public List<Article> getArticlesByJobGroup(String jobgroup, Pageable pageable) {
        List<Article> Articles = articleRepository.findArticlesByJobGroup(jobgroup, pageable);

        return Articles;
    }

    /**
     * 제목타입 검색 결과를 반환
     * @param keyword
     * @return Page<Article>
     */
    public Page<Article> searchArticlesByTitle(String keyword, Pageable pageable) {
        Page<Article> allArticle = articleRepository.findAllByTitleContaining(keyword, pageable);

        return allArticle;
    }

    /**
     * 기업명타입 검색 결과를 반환
     * @param keyword
     * @return Page<Article>
     */
    public Page<Article> searchArticlesByEnterprise(String keyword, Pageable pageable) {
        Page<Article> allArticle = articleRepository.findAllByEnterpriseContaining(keyword, pageable);

        return allArticle;
    }

    @Scheduled(cron = "00 40 12 * * *")
    public void SchedulingTest() throws InterruptedException {
        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ArticleService] : [UpdateArticle]");
        System.out.println("[Started] : " + getNowDateTime24());
        System.out.println("=======================================");
        System.out.println("\n");

        updateNewArticle();

        System.out.println("\n");
        System.out.println("=======================================");
        System.out.println("[ArticleService] : [UpdateArticle]");
        System.out.println("[Ended] : " + getNowDateTime24());
        System.out.println("=======================================");
        System.out.println("\n");
    }

    public void updateViewCount(String originalId) {
        articleRepository.updateViewCountByOriginalId(originalId);
    }
}
