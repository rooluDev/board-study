package com.study.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Board Select 시 필요한 정보들
 */
@Getter
@Setter
@Builder
public class BoardSelectCondition {
    private Timestamp startDate;
    private Timestamp endDate;
    private int categoryId;
    private String searchText;
    private int pageSize;
    private int startRow;
}
