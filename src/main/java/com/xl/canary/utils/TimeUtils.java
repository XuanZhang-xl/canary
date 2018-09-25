package com.xl.canary.utils;

import com.xl.canary.enums.TimeZoneEnum;

import java.util.Calendar;
import java.util.TimeZone;

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

    public static Calendar truncateToDay (Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar;
    }

    public static Long truncateToHour (Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 计算间隔天数, 不足一天算一天
     * @param now
     * @param shouldPayTime
     * @param timeZone
     * @return
     */
    public static int passDays(Long now, Long shouldPayTime, Integer timeZone) {
        Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
        todayCalendar.setTimeInMillis(now);
        todayCalendar = TimeUtils.truncateToDay(todayCalendar);
        Calendar shouldPayCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
        shouldPayCalendar.setTimeInMillis(shouldPayTime);
        shouldPayCalendar = TimeUtils.truncateToDay(shouldPayCalendar);
        Long temp = (todayCalendar.getTimeInMillis() - shouldPayCalendar.getTimeInMillis()) / EssentialConstance.DAY_MILLISECOND;
        return temp.intValue() + 1;
    }
}
