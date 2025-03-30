package com.study.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Timestamp Class Utils
 */
public class TimestampUtils {

    /**
     * Timestamp를 format형식의 String으로 변환하는 메소드
     *
     * @param timestamp timestamp
     * @param format    format형식
     * @return format형식의 string 값
     */
    public static String timestampToStringFormat(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(timestamp);
    }
}
