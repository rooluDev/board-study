package com.study.connection;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        String dateString = "2024-01-20";

        try {
            // SimpleDateFormat을 사용하여 문자열을 Date로 파싱
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(dateString);

            // Date를 Timestamp로 변환
            Timestamp timestamp = new Timestamp(parsedDate.getTime());

            System.out.println("Original String: " + dateString);
            System.out.println("Converted Timestamp: " + timestamp);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
