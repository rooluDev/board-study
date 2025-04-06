
package com.study.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Category Dto
 */
@Getter
@Setter
@Builder
public class CategoryDTO {
    private int categoryId;
    private String categoryName;
}
