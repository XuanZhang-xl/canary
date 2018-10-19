package com.xl.canary.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 账单类型
 * created by XUAN on 2018/09/02
 */
public enum BillTypeEnum {

    /**
     * 借款
     */
    LOAN_ORDER(),

    /**
     * 还款
     */
    PAY_ORDER(),

    /**
     * 优惠券
     */
    COUPON(),

    /**
     * 策略
     */
    STRATEGY(),

    ;

    /**
     * 支持还原的type
     */
    public static final List<BillTypeEnum> RECOVERABLE_TYPE = Arrays.asList(PAY_ORDER, COUPON);

    /**
     * 来源type
     */
    public static final List<BillTypeEnum> SOURCE = Arrays.asList(PAY_ORDER, COUPON, STRATEGY);

    /**
     * 目标type
     */
    public static final List<BillTypeEnum> DESTINATION = Arrays.asList(LOAN_ORDER, COUPON, STRATEGY);
}
