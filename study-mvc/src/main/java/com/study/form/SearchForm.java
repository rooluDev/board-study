package com.study.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Board 검색시 데이터
 */
@Getter
@Setter
public class SearchForm {
    private String startDate;
    private String endDate;
    private int categoryId;
    private String searchText;

    public SearchForm() {
        startDate = getOneYearAgoTime();
        endDate = getCurrentTime();
        categoryId = -1;
    }

    /**
     * 현재시간 받기
     * @return
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 1년전 시간 받기
     * @return
     */
    private String getOneYearAgoTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
}
