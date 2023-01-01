package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.dto.ArticleDto;
import com.spdrtr.nklcb.repository.ArticleRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class Crawling {
    public static WebDriver driver;
    private final ArticleRepository articleRepository;
    public static void process(String url) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/minha/util/chromedriver");
        driver = new ChromeDriver();
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(3000); //브라우저 로딩될때까지 잠시 기다린다.
        //TODO: 카테고리 크롤링 시 로그인 요구함 해결 대책 마련
    }

    public static List<String> getData(String elmName) {
        List<String> list = new ArrayList<>();

        List<WebElement> elements = driver.findElements(By.className(elmName));
        for (WebElement element : elements) {
            list.add(element.getText());
        }
        return list;
    }

    public static WebElement findButton(String elmName) {
        WebElement element = driver.findElement(By.className(elmName));
        return element;
    }

    public static List<WebElement> findButtons(String elmName) {
        List<WebElement> element = driver.findElements(By.className(elmName));
        return element;
    }
    @Autowired
    public Crawling(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void saveArticle() throws InterruptedException {
        String url = "https://www.wanted.co.kr/wdlist?country=kr&job_sort=job.popularity_order&years=-1&locations=all";
        process(url);
        List<String> title = new ArrayList<>();
        List<String> enterprise = new ArrayList<>();
        List<String> locate = new ArrayList<>();
        List<String> reward = new ArrayList<>();
        try {
            title = getData("job-card-position");
            enterprise = getData("job-card-company-name");
            locate = getData("job-card-company-location");
            reward = getData("reward");
        } catch (Exception o) {
        }
//        driver.quit();

        for (int i = 0; i < title.size(); i++) {
            System.out.println(i + 1);
            System.out.println("title:" + title.get(i));
            System.out.println("enterprise:" + enterprise.get(i));
            System.out.println("locate:" + locate.get(i));
            System.out.println("reward:" + reward.get(i) + "\n");
            ArticleDto dto = ArticleDto.builder()
                    .title(title.get(i))
                    .enterprise(enterprise.get(i))
                    .locate(locate.get(i))
                    .reward(reward.get(i))
                    .build();
            articleRepository.save(dto.toEntity());
        }
    }
}
