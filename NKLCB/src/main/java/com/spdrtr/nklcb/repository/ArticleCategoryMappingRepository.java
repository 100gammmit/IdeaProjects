package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.ArticleCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCategoryMappingRepository extends JpaRepository<ArticleCategoryMapping, Long> {
}
