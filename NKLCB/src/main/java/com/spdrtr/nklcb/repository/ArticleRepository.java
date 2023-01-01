package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
