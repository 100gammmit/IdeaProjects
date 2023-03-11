package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByOriginalId(String original_id);
    Page<Article> findAllByTitleContaining(String keyword, Pageable pageable);
    Page<Article> findAllByEnterpriseContaining(String keyword, Pageable pageable);
//    Page<Article> findAllByArticleCategoryMappings(List<ArticleCategoryMapping> articleCategoryMappings, Pageable pageable);
}
