package com.xl.canary.utils;

/**
 * Created by gqwu on 2018/3/29.
 */
public interface LoanLimitation {

    /**
     * 允许的还款日最小值
     */
    int REPAYMENT_DAY_MIN = 1;

    /**
     * 允许的还款日最大值
     */
    int REPAYMENT_DAY_MAX = 28;

    /**
     * 年12期
     */
    int ANNUAL_PERIODS = 12;

    /**
     * 中间计算步骤保留位数
     */
    int INTERMEDIATE_STEP_SCALE = 18;

    /**
     * 结果保留位数
     */
    int RESULT_SCALE = 8;

}
