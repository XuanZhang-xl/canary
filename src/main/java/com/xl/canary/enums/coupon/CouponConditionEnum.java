package com.xl.canary.enums.coupon;

import java.util.Arrays;
import java.util.List;

/**
 * 优惠券使用条件枚举
 * created by XUAN on 2018/09/02
 */
public enum CouponConditionEnum {

    /**
     * 规定可减免期数
     */
    INSTALMENT(),

    /**
     * 规定可减免元素
     */
    ELEMENT(),

    /**
     * 规定可使用的场合
     * 见  UserActionEnum
     */
    OCCASION(),

    /**
     * 规定可用此优惠券的借款币种
     */
    LOAN_CURRENCY(),

    /**
     * 规定可用此优惠券的借款金额
     */
    LOAN_AMOUNT(),

    /**
     * 规定可用此优惠券的还款币种
     */
    PAY_CURRENCY(),

    /**
     * 规定可用此优惠券的还款金额
     */
    PAY_AMOUNT(),

    /**
     * 可用入账次数
     */
    ENTRY_FREQUENCY(),

    // 继续补充

    ;

    /**
     * 影响减免金额大小的元素
     */
    public static final List<CouponConditionEnum> AMOUNT_RELATED = Arrays.asList(INSTALMENT, ELEMENT);

    /**
     * 影响优惠券能不能用的元素
     */
    public static final List<CouponConditionEnum> CONDITION_RELATED = Arrays.asList(OCCASION, LOAN_CURRENCY, LOAN_AMOUNT, PAY_CURRENCY, PAY_AMOUNT);
}
