package com.study.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * DB에서 board 검색 조건
 */
@Getter
@Setter
@Builder
public class BoardSelectCondition {
    private Timestamp startDate;
    private Timestamp endDate;
    private Long categoryId;
    private String searchText;
    private int pageSize;
    private int startRow;
}
