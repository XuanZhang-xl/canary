package com.xl.canary.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 主体枚举
 * Created by xzhang on 2018/9/7.
 */
public enum SubjectEnum {

    /**
     * 当前系统
     * 可以补充其他对接系统等...
     */
    CANARY(),

    /**
     * 用户
     */
    USER(),

    /**
     * 借款订单
     */
    LOAN_ORDER(),

    /**
     * 还款订单
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
     * 可绑定优惠券的主体
     */
    public static final List<SubjectEnum> COUPON_BINDABLE_SUBJECT = Arrays.asList(USER, LOAN_ORDER);


}
