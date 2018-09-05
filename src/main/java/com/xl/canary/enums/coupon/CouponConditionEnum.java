package com.xl.canary.enums.coupon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 优惠券使用条件枚举
 * created by XUAN on 2018/09/02
 */
public enum CouponConditionEnum {

    /**
     * 使用的场合
     * 见  UserActionEnum
     */
    OCCASION(),

    /**
     * 借款币种
     */
    LOAN_CURRENCY(),

    /**
     * 借款金额
     */
    LOAN_AMOUNT(),

    /**
     * 还款币种
     */
    PAY_CURRENCY(),

    /**
     * 还款金额
     */
    PAY_AMOUNT(),



    // 继续补充

    ;
}
