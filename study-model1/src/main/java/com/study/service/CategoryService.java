package com.study.service;

import com.study.dto.Category;
import com.study.repository.CategoryRepository;

import java.util.List;

/**
 * 카테고리 서비스 계층
 */
public class CategoryService {

    private final CategoryRepository categoryRepository = CategoryRepository.getInstance();

    /**
     * 카테고리 리스트 반환
     *
     * @return 카테고리 리스트
     */
    public List<Category> getCategoryList() throws Exception {
        return categoryRepository.getCategoryList();
    }

}
