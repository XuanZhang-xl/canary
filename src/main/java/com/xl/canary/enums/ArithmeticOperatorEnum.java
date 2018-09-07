package com.xl.canary.enums;

/**
 * 算数操作符
 * 如果当前操作符无法满足业务需求, 请自定义操作符并做实现
 * created by XUAN on 2018/09/02
 */
public enum ArithmeticOperatorEnum {

    /**
     * 等于
     */
    EQUAL(),

    /**
     * 不等于
     */
    NOT_EQUAL(),

    /**
     * 大于
     */
    GREAT_THAN(),

    /**
     * 大于等于
     */
    GREAT_THAN_OR_EQUAL(),

    /**
     * 小于
     */
    LESS_THAN(),

    /**
     * 小于等于
     */
    LESS_THAN_OR_EQUAL,

    /**
     * 有限种可能
     */
    IN,

    /**
     * 某两个数之间, 目前是包头包尾的
     */
    BETWEEN(),

    ;
}
