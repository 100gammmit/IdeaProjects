package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
