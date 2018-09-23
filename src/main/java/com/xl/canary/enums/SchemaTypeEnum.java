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
     * 优惠券
     */
    COUPON(),

    /**
     * 策略
     */
    STRATEGY(),

    /**
     * 还款
     */
    REPAY(),


    /*********************************WriteOffTypeEnum, 上下两种类虽不同, 却是冲突的, 暂时合并, 以后有处理不了的情况再改*******************************/

    /**
     * 销账来源
     */
    WRITE_OFF_SOURCE(),

    /**
     * 销账目标
     */
    WRITE_OFF_DESTINATION(),

    ;
}
