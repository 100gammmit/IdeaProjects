package com.spdrtr.nklcb.repository;

import com.spdrtr.nklcb.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c from Category c WHERE c.category_depth2 = :category")
    Category findByCategoryPosition(@Param("category") String category);
}
