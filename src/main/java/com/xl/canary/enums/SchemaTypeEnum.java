package com.xl.canary.enums;

/**
 * schema类型
 * created by XUAN on 2018/09/08
 */
public enum  SchemaTypeEnum {

    /**
     * 订单原始, 下单时schema
     */
    LOAN_ORIGINAL(),

    /**
     * 订单当前, 当前一下还清schema
     */
    LOAN_CURRENT(),

    /**
     * 订单计划, 按期还清schema
     */
    LOAN_PLAN(),

    /**
     * 优惠券作为销帐目标时的类型, 也就是惩罚券
     */
    COUPON_LOAN(),

    /**
     * 优惠券作为销帐来源时的类型
     */
    COUPON_REPAY(),

    /**
     * 策略作为销帐目标时的类型
     */
    STRATEGY_LOAN(),

    /**
     * 策略作为销帐来源时的类型
     */
    STRATEGY_REPAY(),


    ;
}
