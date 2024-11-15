package com.study.controller;


import com.study.dto.CategoryDto;
import com.study.exception.common.success.ApiResponse;
import com.study.exception.common.success.SuccessCode;
import com.study.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * category rest api controller
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 모든 카테고리 리스트 요청
     *
     * @return
     */
    @GetMapping("/categories")
    public ApiResponse<List<CategoryDto>> getCategoryList() {
        return new ApiResponse(categoryService.getCategoryList(), SuccessCode.SELECT_SUCCESS);
    }

    /**
     * 단일 카테고리 요청
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<CategoryDto> getCategory(@PathVariable Long categoryId) throws Exception {
        return new ApiResponse(categoryService.findById(categoryId),SuccessCode.SELECT_SUCCESS);
    }
}
