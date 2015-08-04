package com.lnet.tmsapp.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Timestamp getTimestampNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date toDate(Object source, String format) {
        if (source == null || source == "") return null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(source.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date toDate(Object source) {
       return toDate(source, "yyyy-MM-dd HH:mm:ss");
    }

    public static Timestamp toTimestamp(Object source) {
        if (source == null || source == "") return null;
        Date parsedDate = toDate(source);
        if (parsedDate == null) return null;
        return new Timestamp(parsedDate.getTime());

    }

    public static Timestamp toTimestamp(Object source, String format) {
        if (source == null || source == "") return null;
        Date parsedDate = toDate(source, format);

        if (parsedDate == null) return null;
        return new Timestamp(parsedDate.getTime());
    }
}
