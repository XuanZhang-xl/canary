package com.xl.canary.enums;

/**
 * 优惠券可使用次数
 * 使用次数和使用金额来判断优惠券是否使用完毕
 * 满足一个条件就算只用完毕
 * created by XUAN on 2018/09/02
 */
public enum CouponEntryFrequencyEnum {

    /**
     * 一次
     */
    ONCE(1),

    /**
     * 二次
     */
    TWICE(2),

    /**
     * 三次
     */
    THIRD_TIMES(3),

    /**
     * 无限次
     */
    UNLIMITED(Integer.MAX_VALUE),

    ;
    private Integer frequency;

    CouponEntryFrequencyEnum(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }
}
