package com.xuan.myframework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * com.xuan.myframework.utils
 * Created by xuan on 2017/7/5.
 * version
 * desc
 */

public class NumberUtils {
    public static final String defaultPattern="yyyy-MM-dd HH:mm";

    public static final String simplePattern="yyyy-MM-dd";

    public static final String SUNDAY_TYPE = "周日";

    public static final String MONDAY_TYPE = "周一";

    public static final String TUESDAY_TYPE = "周二";

    public static final String WEDNESDAY_TYPE = "周三";

    public static final String THURSDAY_TYPE = "周四";

    public static final String FRIDAY_TYEP = "周五";

    public static final String SATURDAY_TYPE = "周六";

    public static String convertThousandsString(int num) {

        if (num < 100000) {
            return String.valueOf(num);
        }
        String unit = "万";
        double newNum = num / 10000.0;

        String numStr = String.format("%." + 1 + "f", newNum);
        return numStr + unit;
    }

    public static String longFormatString(long time,String pattern){
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    public static long StringFormatLong(String time,String pattern){
        long date=0;
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        try {
            date=format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getWeek(String date) {

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        //获得一个日历
        Calendar calendar = Calendar.getInstance();
        //设置当前时间,月份是从0月开始计算
        calendar.set(year, month - 1, day);
        //星期表示1-7，是从星期日开始
        int number = calendar.get(Calendar.DAY_OF_WEEK);
        return getWeekDay(number);
    }


    private static String getWeekDay(int dayForWeek) {

        if (dayForWeek == 1) {
            return "周日";
        } else if (dayForWeek == 2) {
            return "周一";
        } else if (dayForWeek == 3) {
            return "周二";
        } else if (dayForWeek == 4) {
            return "周三";
        } else if (dayForWeek == 5) {
            return "周四";
        } else if (dayForWeek == 6) {
            return "周五";
        } else if (dayForWeek == 7) {
            return "周六";
        } else {
            return "";
        }
    }

}
