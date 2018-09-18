package com.xl.canary.enums.coupon;

import java.util.Arrays;
import java.util.List;

/**
 * 优惠券使用条件枚举
 *
 * ${LOAN_CURRENCY} 为变量
 * created by XUAN on 2018/09/02
 */
public enum CouponConditionEnum {

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

    /**
     * 限定用户
     */
    USER(),

    /**
     * 限定用于某个借款订单
     */
    LOAN_ORDER_ID,

    /**
     * 特殊的限制条件, 这儿只是举个例子
     */
    SPECIAL(),

    // 继续补充

    ;

    /**
     * 影响优惠券能不能用的元素
     */
    public static final List<CouponConditionEnum> CONDITION_RELATED = Arrays.asList(OCCASION, LOAN_CURRENCY, LOAN_AMOUNT, PAY_CURRENCY, PAY_AMOUNT);
}
