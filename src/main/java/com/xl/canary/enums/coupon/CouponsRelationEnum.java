package com.xl.canary.enums.coupon;

/**
 * 优惠券关系类型
 * 兼容时, 优惠券相加即可, 超过应还上限则相当于这一部分全减免
 * 但是当WeightEnum参数不同时, 应抛异常
 * created by XUAN on 2018/09/02
 */
public enum CouponsRelationEnum {

    /**
     * 兼容，随便用
     */
    COMPATIBILITY(),

    /**
     * 自我兼容， 多张自己可以一起用
     */
    SELF_COMPATIBILITY(),

    /**
     * 互斥， 只能使用一张
     */
    EXCLUSION(),

    /**
     * 自我互斥， 不可和同类型一起用
     */
    SELF_EXCLUSION(),

}
