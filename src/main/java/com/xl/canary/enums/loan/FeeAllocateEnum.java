package com.xl.canary.enums.loan;

/**
 * 订单费用分配方法
 * created by XUAN on 2018/08/25
 */
public enum FeeAllocateEnum {

    /**
     * 直接分配给某个分期, 默认第一期
     */
    FOLLOW_INSTALMENT(),

    /**
     * 每个分期平均分配
     */
    AVERAGE_IN_INSTALMENT(),

    ;
}
