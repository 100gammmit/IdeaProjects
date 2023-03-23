package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByOriginalId(String original_id);
    Page<Article> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Article> findAllByEnterpriseContaining(String keyword, Pageable pageable);
    @Query(value = "select * from Article order by RAND() limit 8", nativeQuery = true)
    List<Article> findArticlesInMain();

    @Query("select ac from Article ac " +
        "left outer join ArticleCategoryMapping acm on acm.article.id = ac.id " +
            "where acm.category.id = :id")
    Page<Article> findArticlesByCategoryId(@Param("id") Long id, Pageable pageable);
}
