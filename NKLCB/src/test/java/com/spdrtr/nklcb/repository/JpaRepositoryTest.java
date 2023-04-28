package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import com.spdrtr.nklcb.domain.Category;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Jpa 연결 테스트")
class JpaRepositoryTest {
    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;
    private ArticleCategoryMappingRepository articleCategoryMappingRepository;
    @PersistenceContext
    private EntityManager em;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCategoryMappingRepository articleCategoryMappingRepository,
            @Autowired CategoryRepository categoryRepository ) {
        this.articleRepository = articleRepository;
        this.articleCategoryMappingRepository = articleCategoryMappingRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    public void idRestart() {
        em.createNativeQuery("ALTER TABLE ARTICLE ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE ARTICLE_CATEGORY_MAPPING ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE CATEGORY ALTER COLUMN ID RESTART WITH 1").executeUpdate();

        insertData();
    }

    @Test
    @DisplayName("게시글과 카테고리가 정상적으로 매핑되어 DB에 저장되는지 확인")
    void ArticleCategoryMappingTest() {
        //given
        //when

        //then
        assertThat(articleCategoryMappingRepository.findById(1L).get().getArticle()).isEqualTo(articleRepository.findById(1L).get());
        assertThat(articleCategoryMappingRepository.findById(1L).get().getCategory()).isEqualTo(categoryRepository.findById(1L).get());
        assertThat(articleCategoryMappingRepository.findById(2L).get().getArticle()).isEqualTo(articleRepository.findById(2L).get());
        assertThat(articleCategoryMappingRepository.findById(2L).get().getCategory()).isEqualTo(categoryRepository.findById(2L).get());
    }

    @Test
    @DisplayName("findbyOriginalId 테스트")
    void findArticle() {


        assertThat(articleRepository.findByOriginalId("12").get().getTitle()).isEqualTo("백엔드");
        assertThat(articleRepository.findByOriginalId("20")).isEmpty();
    }

    @Test
    @DisplayName("findArticlesByCategoryId 테스트")
    void findArticlesByCategoryId() {
        // given

        // when
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        // then
        assertThat(articleRepository.findArticlesByCategoryId(1L, pageable).get().findFirst().get()).isEqualTo(articleRepository.findById(1L).get());
        assertThat(articleRepository.findArticlesByCategoryId(2L, pageable).getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("findArticlesByCategoryPosition 테스트")
    void findArticlesByCategoryPosition() {
        // given

        // when
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));
        // then
        assertThat(articleRepository.findArticlesByCategoryPosition("소프트", pageable).get().findFirst().get()).isEqualTo(articleRepository.findById(1L).get());
        assertThat(articleRepository.findArticlesByCategoryPosition("프론트", pageable).getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("updateViewCount 테스트")
    void updateViewCount() {
        // given
        Article article = articleRepository.findById(1L).get();
        int beforeCount = article.getView_count();
        String oriId = article.getOriginalId();
        // when
        articleRepository.updateViewCountByOriginalId(oriId);
        // then
        assertThat(articleRepository.findById(1L).get().getView_count()).isEqualTo(beforeCount+1);
    }

    void insertData() {
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

        Article article1 = Article.builder()
                .title("프론트")
                .enterprise("카카오")
                .locate("경기.판교")
                .originalId("13")
                .reward("100")
                .build();
        articleRepository.save(article1);
        Category category1 = Category.of("개발", "프론트");
        categoryRepository.save(category1);
        ArticleCategoryMapping articleCategoryMapping1 = new ArticleCategoryMapping();
        article1.addMappingWithCategory(articleCategoryMapping1);
        articleCategoryMapping1.takeCategory(category1);
        articleCategoryMappingRepository.save(articleCategoryMapping1);

        Article article2 = Article.builder()
                .title("데이터")
                .enterprise("라인")
                .locate("서울.강남")
                .originalId("13")
                .reward("100")
                .build();
        articleRepository.save(article2);
        ArticleCategoryMapping articleCategoryMapping2 = new ArticleCategoryMapping();
        article2.addMappingWithCategory(articleCategoryMapping2);
        articleCategoryMapping2.takeCategory(category1);
        articleCategoryMappingRepository.save(articleCategoryMapping2);
    }
}