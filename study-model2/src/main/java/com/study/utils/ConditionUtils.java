package com.study.utils;

import com.study.condition.SearchCondition;

/**
 * Condition Utils
 */
public class ConditionUtils {

    /**
     * 검색조건이 없는 경우와 검색조건이 있는 경우에 맞는 SearchCondition 반환
     *
     * @param startDate  처음 검색 기간
     * @param endDate    끝 검색 기간
     * @param categoryId 카테고리
     * @param searchText 검색어
     * @param pageNum    페이지
     * @return 검색조건 객체
     */
    public static SearchCondition parameterToSearchCondition(String startDate, String endDate, String categoryId, String searchText, String pageNum) {
        boolean isInit = startDate == null || endDate == null || categoryId == null || searchText == null;
        if (isInit) {
            return new SearchCondition();
        } else {
            if (pageNum == null) {
                pageNum = "1";
            }
            return new SearchCondition(startDate, endDate, categoryId, searchText, pageNum);
        }
    }
}
