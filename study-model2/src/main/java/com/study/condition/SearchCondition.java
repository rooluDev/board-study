package com.study.condition;

import com.study.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Getter
@Setter
public class SearchCondition {
    private Timestamp startDate = this.getOneYearAgoDate();
    private Timestamp endDate = this.getCurrentDate();
    private int categoryId;
    private String searchText;


    private Timestamp getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(5, 1);
        Date oneDayPlus = calendar.getTime();
        return StringUtils.parseToTimestamp(dateFormat.format(oneDayPlus));
    }

    private Timestamp getOneYearAgoDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(1, -1);
        Date oneYearAgo = calendar.getTime();
        return StringUtils.parseToTimestamp(dateFormat.format(oneYearAgo));
    }
}
