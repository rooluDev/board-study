package com.study.service;

import com.study.dto.CategoryDTO;
import com.study.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 카테고리 서비스
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    /**
     * 카테고리 List 가져오기
     *
     * @return 카테고리 리스트
     */
    public List<CategoryDTO> getCategoryList(){
        return categoryMapper.getCategory();
    }
}
