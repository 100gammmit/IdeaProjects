package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.dto.ArticleDto;
import com.spdrtr.nklcb.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.spdrtr.nklcb.service.Crawling.getData;
import static com.spdrtr.nklcb.service.Crawling.process;

@Service
@Transactional
public class SaveArticle {
    private final ArticleRepository articleRepository;
    @Autowired
    public SaveArticle(ArticleRepository articleRepository) {
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
