package com.xl.canary.utils;

import java.util.Calendar;

/**
 * 处理时间
 * created by XUAN on 2018/09/09
 */
public class TimeUtils {

    public static Long truncateToDay (Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTimeInMillis();
    }

}
