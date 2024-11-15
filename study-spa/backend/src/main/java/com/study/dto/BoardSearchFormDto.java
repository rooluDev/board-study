package com.study.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 웹페이지에서 Board 검색 조건
 */
@Getter
@Setter
@AllArgsConstructor
public class BoardSearchFormDto {
    private final String startDate;
    private final String endDate;
    private final Long categoryId;
    private final String searchText;
    private final int pageNum;
    private final int pageSize;
}
