package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * CategoryDTO
 */
@Getter
@Setter
@Builder
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
}
