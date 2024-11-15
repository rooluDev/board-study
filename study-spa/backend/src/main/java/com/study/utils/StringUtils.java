package com.study.utils;

import com.study.dto.BoardSearchFormDto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 사용자 정의 StringUtils
 */
public class StringUtils {

    /**
     * (00 : 00 : 00) String -> Timestamp로 캐스팅
     * @param date
     * @return
     */
    public static Timestamp parseToTimestampStart(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MIN);


        return Timestamp.valueOf(endOfDay);
    }

    /**
     * (23 : 59 : 59) String -> Timestamp로 캐스팅
     * @param date
     * @return
     */
    public static Timestamp parseToTimestampEnd(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);


        return Timestamp.valueOf(endOfDay);
    }

    /**
     * 공백과 null check
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        return "".equals(str) || (null == str);
    }

    /**
     * 처음 검색 조건에서 공백으로 넘어오는거 체크
     * @param boardSearchFormDto
     * @return
     */
    public static boolean isBoardFormNull(BoardSearchFormDto boardSearchFormDto){
        if(isNull(boardSearchFormDto.getEndDate())|| isNull(boardSearchFormDto.getStartDate())){
            return true;
        }
        return false;
    }

}
