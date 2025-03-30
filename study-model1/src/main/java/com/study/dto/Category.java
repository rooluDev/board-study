package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 Dto
 */
@Getter
@Setter
@Builder
public class Category {
    private int categoryId;
    private String categoryName;
}
