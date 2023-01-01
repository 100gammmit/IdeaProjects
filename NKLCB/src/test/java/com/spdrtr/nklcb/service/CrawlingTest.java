package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class CrawlingTest {
    @InjectMocks private Crawling cr;
    @Mock private ArticleRepository articleRepository;

    @Test
    void savingArticle() throws InterruptedException {
        cr = new Crawling(articleRepository);
        cr.saveArticle();
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).isNotNull().hasSize(20);
    }

}