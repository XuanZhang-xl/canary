package com.xl.canary.enums;

import com.xl.canary.utils.EssentialConstance;

/**
 * 时间枚举
 * created by XUAN on 2018/08/06
 */
public enum TimeUnitEnum {

    /**
     * 年, 如果订单类型是以年记, 则一年默认360天
     */
    YEAR(EssentialConstance.ANNUAL_DAYS),

    /**
     * 月, 如果订单类型是以月记, 则一个月默认30天
     */
    MONTH(EssentialConstance.MONTH_DAYS),

    /**
     * 星期, 如果订单类型是以星期记, 则一星期默认7天
     */
    WEEK(EssentialConstance.WEEK_DAYS),

    /**
     * 天, 一天, 借款的最小周期
     */
    DAY(EssentialConstance.DAY_DAYS);

    private Integer days;

    TimeUnitEnum(Integer days) {
        this.days = days;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
