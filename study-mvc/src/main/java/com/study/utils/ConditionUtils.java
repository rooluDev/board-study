package com.study.utils;

import com.study.condition.SearchCondition;

/**
 * Condition Custom Utils
 */
public class ConditionUtils {

    /**
     * 검색조건을 유지를 위해 searchCondition 객체를 쿼리스트링으로 변환
     *
     * @param searchCondition 검색조건 객체
     * @return 쿼리스트링
     */
    public static String convertToQueryString(SearchCondition searchCondition) {
        return "startDate=" + searchCondition.getStartDate() +
                "&endDate=" + searchCondition.getEndDate() +
                "&pageNum=" + searchCondition.getPageNum() +
                "&searchText=" + searchCondition.getSearchText() +
                "&categoryId=" + searchCondition.getCategoryId();
    }
}
