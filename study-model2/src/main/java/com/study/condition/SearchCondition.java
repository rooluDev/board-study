package com.study.condition;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * 검색조건
 */
@Getter
@Setter
public class SearchCondition {
    private String startDate;
    private String endDate;
    private int categoryId;
    private String searchText;
    private int pageNum;
    private int startRow;
    private Timestamp startDateTimestamp;
    private Timestamp endDateTimestamp;

    /**
     * 기본 검색 조건 생성자
     */
    public SearchCondition() {
        this.startDate = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.categoryId = -1;
        this.searchText = "";
        this.pageNum = 1;
    }

    public SearchCondition(String startDate, String endDate, String categoryId, String searchText, String pageNum) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = Integer.parseInt(categoryId);
        this.searchText = searchText;
        this.pageNum = Integer.parseInt(pageNum);
    }

    public Timestamp getStartDateTimestamp() {
        LocalDate localStateDate = LocalDate.parse(this.startDate);
        LocalDateTime startDate = localStateDate.atTime(LocalTime.MIN);
        return Timestamp.valueOf(startDate);
    }

    public Timestamp getEndDateTimestamp() {
        LocalDate localEndDate = LocalDate.parse(this.endDate);
        LocalDateTime endDate = localEndDate.atTime(LocalTime.MAX);
        return Timestamp.valueOf(endDate);
    }

    /**
     * OFFSET Getter
     *
     * @return offset
     */
    public int getStartRow() {
        if (pageNum == 1) {
            return 0;
        }
        return (this.pageNum - 1) * 10;
    }
}
