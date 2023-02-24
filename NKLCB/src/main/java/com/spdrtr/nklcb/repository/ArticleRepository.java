package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByOriginalId(String original_id);
}
