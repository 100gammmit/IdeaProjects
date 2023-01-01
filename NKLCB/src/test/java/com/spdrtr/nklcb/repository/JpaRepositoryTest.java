package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import com.spdrtr.nklcb.domain.Category;
import com.spdrtr.nklcb.dto.ArticleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Jpa 연결 테스트")
class JpaRepositoryTest {
    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;
    private ArticleCategoryMappingRepository articleCategoryMappingRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCategoryMappingRepository articleCategoryMappingRepository,
            @Autowired CategoryRepository categoryRepository ) {
        this.articleRepository = articleRepository;
        this.articleCategoryMappingRepository = articleCategoryMappingRepository;
        this.categoryRepository = categoryRepository;
    }

    @Test
    void save테스트(){
        //given
        long previousCount = articleRepository.count();
        //when
        ArticleDto articleDto = ArticleDto.builder()
                .title("백엔드")
                .enterprise("네이버")
                .locate("경기.판교")
                .reward("100만원")
                .build();
        articleRepository.save(articleDto.toEntity());

        //then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);
    }


}