package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCategoryMappingRepository extends JpaRepository<ArticleCategoryMapping, Long> {
    List<ArticleCategoryMapping> findAllByCategoryId(Long category_id);
}
