
package com.study.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimestampUtils {

    public static String parseToString(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String stringTimestamp = simpleDateFormat.format(timestamp);
        return stringTimestamp;
    }
}
