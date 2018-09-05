package com.xl.canary.enums.pay;

/**
 * 销帐类型
 * created by XUAN on 2018/09/02
 */
public enum WriteOffTypeEnum {

    /**
     * 销帐来源
     * 一般来源有: 还款, 优惠券, 策略
     */
    SOURCE(),

    /**
     * 销帐目标
     * 一般目标有: 借款, 策略
     * 优惠券只能作为销帐来源, 优惠不可能越优惠越多
     */
    DESTINATION(),
}
