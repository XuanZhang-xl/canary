package com.xl.canary.enums;

/**
 * schema类型
 * TODO:如果加入优惠券, 策略, 则会有变化
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


    ;
}
