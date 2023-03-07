package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import com.spdrtr.nklcb.domain.Category;
import com.spdrtr.nklcb.dto.ArticleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
                .originalId("12")
                .reward("100")
                .build();
        articleRepository.save(articleDto.toEntity());

        //then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);
    }

    @Test
    @DisplayName("게시글과 카테고리가 정상적으로 매핑되어 DB에 저장되는지 확인")
    void ArticleCategoryMappingTest() {
        //given
        long previousCount = articleCategoryMappingRepository.count();
        //when
        Article article = Article.builder()
                .title("백엔드")
                .enterprise("네이버")
                .locate("경기.판교")
                .originalId("12")
                .reward("100")
                .build();
        articleRepository.save(article);
        Category category = Category.of("개발", "소프트");
        categoryRepository.save(category);

        ArticleCategoryMapping articleCategoryMapping = new ArticleCategoryMapping();
        article.addMappingWithCategory(articleCategoryMapping);
        articleCategoryMapping.takeCategory(category);
        articleCategoryMappingRepository.save(articleCategoryMapping);

        //then
        assertThat(articleCategoryMappingRepository.count()).isEqualTo(previousCount+1);
        assertThat(articleRepository.findById(1L).get().getArticleCategoryMappings()).isNotNull();
        assertThat(categoryRepository.findById(1L).get().getArticleCategoryMappings()).isNotNull();
    }

    @Test
    @DisplayName("findbyOriginalId 테스트")
    void findArticle() {
        ArticleDto dto = ArticleDto.builder()
                .title("제목")
                .enterprise("기업")
                .originalId("12")
                .locate("위치")
                .reward("100")
                .build();
        articleRepository.save(dto.toEntity());

        assertThat(articleRepository.findByOriginalId("12").get().getTitle()).isEqualTo("제목");
        assertThat(articleRepository.findByOriginalId("13")).isEmpty();
    }

}