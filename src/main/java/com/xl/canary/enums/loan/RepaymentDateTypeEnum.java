package com.xl.canary.enums.loan;

import com.xl.canary.enums.TimeUnitEnum;

/**
 * 还款日期类型枚举
 * created by XUAN on 2018/08/19
 */
public enum RepaymentDateTypeEnum {

    /**
     * 每年有固定还款日
     */
    FIX_YEAR_REPAYMENT_DATE(TimeUnitEnum.YEAR),

    /**
     * 每月有有固定还款日
     */
    FIX_MONTH_REPAYMENT_DATE(TimeUnitEnum.MONTH),

    /**
     * 每周有固定还款日
     */
    FIX_WEEK_REPAYMENT_DATE(TimeUnitEnum.WEEK),

    /**
     * 固定还款间隔, 间隔为1月(30天)
     */
    FIX_MONTH_INTERVAL(TimeUnitEnum.MONTH),

    // 如果无法满足, 则继续补充

    ;

    /**
     * 时间单位
     */
    private TimeUnitEnum timeUnit;

    public TimeUnitEnum getTimeUnit() {
        return timeUnit;
    }

    RepaymentDateTypeEnum(TimeUnitEnum timeUnit) {
        this.timeUnit = timeUnit;
    }
}
