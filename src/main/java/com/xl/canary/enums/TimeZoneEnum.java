package com.xl.canary.enums;

import com.xl.canary.exception.GeneralException;
import com.xl.canary.exception.InnerException;

/**
 * Created by xzhang on 2018/8/23.
 */
public enum TimeZoneEnum {
    /**
     * 所有时区
     */
    GMT_14(-14, "GMT-14"),
    GMT_13(-13, "GMT-13"),
    GMT_12(-12, "GMT-12"),
    GMT_11(-11, "GMT-11"),
    GMT_10(-10, "GMT-10"),
    GMT_9(-9, "GMT-9"),
    GMT_8(-8, "GMT-8"),
    GMT_7(-7, "GMT-7"),
    GMT_6(-6, "GMT-6"),
    GMT_5(-5, "GMT-5"),
    GMT_4(-4, "GMT-4"),
    GMT_3(-3, "GMT-3"),
    GMT_2(-2, "GMT-2"),
    GMT_1(-1, "GMT-1"),
    GMT0(0, "GMT0"),
    GMT1(1, "GMT+1"),
    GMT2(2, "GMT+2"),
    GMT3(3, "GMT+3"),
    GMT4(4, "GMT+4"),
    GMT5(5, "GMT+5"),
    GMT6(6, "GMT+6"),
    GMT7(7, "GMT+7"),
    GMT8(8, "GMT+8"),
    GMT9(9, "GMT+9"),
    GMT10(10, "GMT+10"),
    GMT11(11, "GMT+11"),
    GMT12(12, "GMT+12"),
    GMT13(13, "GMT+13"),
    GMT14(14, "GMT+14"),
    ;

    private Integer timeZone;

    private String zoneId;

    public Integer getTimeZone() {
        return timeZone;
    }

    public String getZoneId() {
        return zoneId;
    }

    TimeZoneEnum(Integer timeZone, String zoneId) {
        this.timeZone = timeZone;
        this.zoneId = zoneId;
    }

    public static String getZoneId(Integer timeZone) {
        if (timeZone == null) {
            throw new InnerException(ResponseNutEnum.NO_TIME_ZONE);
        }
        for (TimeZoneEnum timeZoneEnum : TimeZoneEnum.values()) {
            if (timeZone.equals(timeZoneEnum.timeZone)) {
                return timeZoneEnum.zoneId;
            }
        }
        throw new InnerException(ResponseNutEnum.NO_TIME_ZONE);
    }


}
