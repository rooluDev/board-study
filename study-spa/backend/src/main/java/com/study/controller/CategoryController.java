package com.study.controller;

import com.study.dto.CategoryDto;
import com.study.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * category rest api controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 모든 카테고리 리스트 요청
     *
     * @return {
     * categoryList: []
     * }
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategoryList() {
        List<CategoryDto> categoryList = categoryService.getCategoryList();

        return ResponseEntity.ok().body(categoryList);
    }
}
