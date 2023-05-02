package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByOriginalId(String original_id);
    Page<Article> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Article> findAllByEnterpriseContaining(String keyword, Pageable pageable);

    @Query("select ac from Article ac " +
        "left outer join ArticleCategoryMapping acm on acm.article.id = ac.id " +
            "where acm.category.id = :id")
    Page<Article> findArticlesByCategoryId(@Param("id") Long id, Pageable pageable);

    @Query("select ac from Article ac " +
            "left outer join ArticleCategoryMapping acm on acm.article.id = ac.id " +
            "left outer join Category cg on cg.id = acm.category.id " +
            "where cg.category_depth2 = :position")
    Page<Article> findArticlesByCategoryPosition(@Param("position") String position, Pageable pageable);

    @Query("select distinct(ac) from Article ac "+
            "left outer join ArticleCategoryMapping acm on acm.article.id = ac.id " +
            "left outer join Category cg on cg.id = acm.category.id " +
            "where cg.category_depth1 = :jobgroup")
    List<Article> findArticlesByJobGroup(@Param("jobgroup") String jobgroup, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Article ac set ac.view_count = ac.view_count + 1 where ac.originalId = :originalId")
    int updateViewCountByOriginalId(@Param("originalId") String originalId);
}
