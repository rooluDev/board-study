package com.study.entity;

import com.study.dto.CategoryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Category Entity
 */
@Getter
@Builder
public class Category {
    private final Long categoryId;
    private final String categoryName;

    /**
     * entity -> dto
     * @return
     */
    public CategoryDto toCategoryDto(){
        return CategoryDto.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .build();
    }
}
