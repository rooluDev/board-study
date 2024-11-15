package com.study.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    public static boolean isNull(String str) {
        return str == null || "".equals(str);
    }

    public static Timestamp parseToTimestamp(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp parseTimestamp = null;

        try {
            Date parseDate = simpleDateFormat.parse(date);
            parseTimestamp = new Timestamp(parseDate.getTime());
        } catch (ParseException var4) {
            var4.printStackTrace();
        }

        return parseTimestamp;
    }
}
