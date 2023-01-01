package com.spdrtr.nklcb.dto;

import com.spdrtr.nklcb.domain.Category;

import java.io.Serializable;

/**
 * A DTO for the {@link com.spdrtr.nklcb.domain.Category} entity
 */
public record CategoryDto(long id, String category_depth1, String category_depth2) implements Serializable {

    public static CategoryDto from(Category entity){
        return new CategoryDto(
                entity.getId(),
                entity.getCategory_depth1(),
                entity.getCategory_depth1()
        );
    }

    public Category toEntity() {
        return Category.of(category_depth1, category_depth2);
    }
}