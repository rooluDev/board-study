package com.study.mapper;

import com.study.dto.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CategoryDTO Mapper
 */
@Mapper
public interface CategoryMapper {
    /**
     * Category 조회
     * @return
     */
    List<CategoryDTO> getCategory();

    /**
     * pk로 categoryName 조회
     * @param categoryId
     * @return
     */
    String findById(Long categoryId);
}
