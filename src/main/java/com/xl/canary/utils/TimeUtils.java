package com.xl.canary.utils;

import com.xl.canary.enums.TimeZoneEnum;
import com.xl.canary.exception.DateCalaulateException;

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
     * @param from
     * @param to
     * @param timeZone
     * @return
     */
    public static int passDays(Long from, Long to, Integer timeZone) throws DateCalaulateException {
        Calendar fromCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
        fromCalendar.setTimeInMillis(from);
        fromCalendar = TimeUtils.truncateToDay(fromCalendar);
        Calendar toCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
        toCalendar.setTimeInMillis(to);
        toCalendar = TimeUtils.truncateToDay(toCalendar);
        if (fromCalendar.after(toCalendar)) {
            throw new DateCalaulateException("begin必须在end之前");
        }
        Long temp = (toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / EssentialConstance.DAY_MILLISECOND;
        return temp.intValue() + 1;
    }

    /**
     * 计算间隔天数, 不足一天算一天
     * @param begin
     * @param end
     * @return
     */
    public static int passDays(Calendar begin, Calendar end) throws DateCalaulateException {
        if (begin.after(end)) {
            throw new DateCalaulateException("begin必须在end之前");
        }
        begin = TimeUtils.truncateToDay(begin);
        end = TimeUtils.truncateToDay(end);
        long diffDays = (end.getTimeInMillis() - begin.getTimeInMillis()) / EssentialConstance.DAY_MILLISECOND;
        return (int) diffDays + 1;
    }

    /**
     * utc时间戳---> 本地时间
     * 截取至当前 的 天
     * @param time      utc时间
     * @param timeZone  时区
     * @return
     */
    public static Calendar localCalendarTruncateDay(Long time, Integer timeZone) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
        calendar.setTimeInMillis(time);
        calendar = TimeUtils.truncateToDay(calendar);
        return calendar;
    }

    /**
     * utc时间戳---> 本地时间
     * @param time      utc时间
     * @param timeZone  时区
     * @return
     */
    public static Calendar getLocalCalendar(Long time, Integer timeZone) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
        calendar.setTimeInMillis(time);
        return calendar;
    }
}
