package com.study.service;

import com.study.dto.CategoryDto;
import com.study.exception.CategoryNotFoundException;
import com.study.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 관련 비지니스 로직
 */
@Service
public class CategoryService {
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryMapper categoryMapper){
        this.categoryMapper = categoryMapper;
    }

    /**
     * DB에 있는 Category 전부 가져오기
     * @return
     */
    public List<CategoryDto> getCategoryList(){
        // wrapper class
        List<CategoryDto> categoryDtoList = categoryMapper.getCategoryList().stream()
                .map(entity-> entity.toCategoryDto())
                .collect(Collectors.toList());

        return categoryDtoList;
    }

    /**
     * pk로 Category 하나 가져오기
     * @param categoryId
     * @return
     */
    public CategoryDto findById(Long categoryId) throws CategoryNotFoundException{
        return categoryMapper.findById(categoryId).orElseThrow(()-> new CategoryNotFoundException()).toCategoryDto();
    }
}
