package com.xl.canary.enums;

/**
 * 元素类型
 * 用于决定期数间的入账秩序
 * created by XUAN on 2018/08/20
 */
public enum LoanOrderElementTypeEnum {

    /**
     * 不分期的元素的属性
     */
    NULL(),

    /**
     * 一般入账秩序, 即跟随期数, 这一期入账完成后, 剩下的进入下一个轮回
     */
    GENERAL_INSTALMENT(),

    /**
     * 优先入账, 一旦轮到, 则所有期数一并全部入账
     */
    PRIVILEGE(),

}
