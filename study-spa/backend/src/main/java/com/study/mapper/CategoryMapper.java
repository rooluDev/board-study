package com.study.mapper;

import com.study.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * Category Mapper
 */
@Mapper
public interface CategoryMapper {
    /**
     * DB에 있는 모든 카테고리 리스트 검색
     * @return
     */
    List<Category> getCategoryList();

    /**
     * pk로 카테고리 검색
     * @param categoryId
     * @return
     */
    Optional<Category> findById(Long categoryId);
}
