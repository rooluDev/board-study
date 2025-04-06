
package com.study.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Timestamp Utils
 */
public class TimestampUtils {

    /**
     * Timestamp 형식을 format 형식의 String으로 변환
     *
     * @param timestamp Timestamp
     * @param format fomat
     * @return format 형식의 String
     */
    public static String parseToString(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(timestamp);
    }
}
