package com.xl.canary.enums.coupon;

import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.enums.WeightEnum;

/**
 * 优惠券类型
 * 1. 是否只能单独使用?
 *      1.1 如果可以同时使用, 如何处理与其他优惠券之间的关系?
 *      答: 见  CouponsRelationEnum
 * 2. 减免哪一部分?
 *      答: 应在类型中规定
 * 3. 减免多少?
 *      答: 这不是类型决定的, 是优惠券的固有属性
 * 4. 什么时候减免?
 *      答: 1. 用户手动添加, 还款时减免
 *          2. 可以直接使用, 就像代金券
 *          3. 时机, 可以多次减免, 也可能只能有一次
 * created by XUAN on 2018/09/02
 */
public enum CouponTypeEnum {

    /**
     * 利息减免券
     */
    INTEREST_COUPON("利息减免券[使用限制说明等]",
            CouponsRelationEnum.COMPATIBILITY,
            LoanOrderElementEnum.INTEREST,
            CouponEntryFrequencyEnum.ONCE,
            WeightEnum.PERCENT);

    /**
     * 描述
     */
    private String desc;

    /**
     * 与其他优惠券关系
     */
    private CouponsRelationEnum couponsRelation;

    /**
     * 作用于的元素
     */
    private LoanOrderElementEnum loanOrderElement;

    /**
     * 入账方式
     */
    private CouponEntryFrequencyEnum couponEntryFrequency;

    /**
     * 比重类型
     * 注意: 当两张优惠券同时使用, 作用的元素一致而比重类型不一致时, 会抛异常
     */
    private WeightEnum weight;

    CouponTypeEnum(String desc, CouponsRelationEnum couponsRelation, LoanOrderElementEnum loanOrderElement, CouponEntryFrequencyEnum couponEntryFrequency, WeightEnum weight) {
        this.desc = desc;
        this.couponsRelation = couponsRelation;
        this.loanOrderElement = loanOrderElement;
        this.couponEntryFrequency = couponEntryFrequency;
        this.weight = weight;
    }

    public String getDesc() {
        return desc;
    }

    public CouponsRelationEnum getCouponsRelation() {
        return couponsRelation;
    }

    public LoanOrderElementEnum getLoanOrderElement() {
        return loanOrderElement;
    }

    public CouponEntryFrequencyEnum getCouponEntryFrequency() {
        return couponEntryFrequency;
    }
}
