package com.xl.canary.enums;

/**
 * schema类型
 * TODO:如果加入优惠券, 策略, 则会有变化
 * created by XUAN on 2018/09/08
 */
public enum  SchemaTypeEnum {

    /**
     * 原始, 下单时schema
     */
    ORIGINAL(),

    /**
     * 当前, 当前一下还清schema
     */
    CURRENT(),

    /**
     * 计划, 按期还清schema
     */
    PLAN(),


    ;
}
