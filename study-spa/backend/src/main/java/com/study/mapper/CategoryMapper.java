package com.study.mapper;

import com.study.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CategoryDto Mapper
 */
@Mapper
public interface CategoryMapper {

    /**
     * Category 조회
     *
     * @return DB에 저장된 카테고리 리스트
     */
    List<CategoryDto> getCategory();
}
