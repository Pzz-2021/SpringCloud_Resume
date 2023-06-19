package com.resume.base.utils;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

public class DateUtil {
    private static final long MIN_TIME = 1000L * 60;
    private static final long HOUR_TIME = MIN_TIME * 60;
    private static final long DAY_TIME = HOUR_TIME * 24;
    private static final long YEAR_TIME = DAY_TIME * 365;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat getSimpleDateFormat1() {
        return SIMPLE_DATE_FORMAT1;
    }

    public static SimpleDateFormat getSimpleDateFormat2() {
        return SIMPLE_DATE_FORMAT2;
    }

    /**
     * 返回现在的年月日 yyyy-MM-dd
     * @ pp
     */
    public static String getDate1(){
        return SIMPLE_DATE_FORMAT1.format(new Date());
    }

    /**
     * 返回现在 之后dayCount天 的年月日 yyyy-MM-dd
     * @ pp
     */
    public static String getDate1(int dayCount){
        return SIMPLE_DATE_FORMAT1.format(new Date().getTime() + dayCount * DAY_TIME);
    }

    /**
     * 返回现在的年月日时分秒 yyyy-MM-dd HH:mm:ss
     * @ pp
     */
    public static String getDate2(){
        return SIMPLE_DATE_FORMAT2.format(new Date());
    }


    // 给一个Date数据，返回该数据所在周的星期一
    public static LocalDate getMondayOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int daysUntilMonday = DayOfWeek.MONDAY.getValue() - dayOfWeek.getValue();
        if (daysUntilMonday > 0) {
            daysUntilMonday -= 7;
        }
        return date.plusDays(daysUntilMonday);
    }

    // 给一个 String 类型转成 String，解析失败返回 null
    public static String stringToDateFormat(String date) {
        Date parse = null;
        try {
            parse = getSimpleDateFormat1().parse(date);
        } catch (Exception e) {
            return null;
        }
        return getSimpleDateFormat1().format(parse);
    }
}
