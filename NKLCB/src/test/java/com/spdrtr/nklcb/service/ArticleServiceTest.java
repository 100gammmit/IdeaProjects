package com.spdrtr.nklcb.service;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.dto.ArticleDto;
import com.spdrtr.nklcb.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleServiceTest {
    @Autowired
    private ArticleService cr;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("ArticleRepository save, findbyid 테스트")
    void saveAndFindArticle() {
        ArticleDto dto = ArticleDto.builder()
                .title("제목")
                .enterprise("기업")
                .locate("위치")
                .reward(100)
                .build();
        articleRepository.save(dto.toEntity());

        assertThat(articleRepository.findById(1L).get().getTitle()).isEqualTo("제목");
    }



}