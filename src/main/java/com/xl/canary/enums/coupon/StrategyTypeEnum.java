package com.xl.canary.enums.coupon;

import com.xl.canary.enums.SubjectEnum;
import com.xl.canary.enums.WeightEnum;

/**
 * 策略类型
 * created by XUAN on 2018/09/02
 */
public enum StrategyTypeEnum {

    /**
     * 借还款币种不同就要用的策略
     */
    DIFF_COIN(CouponsRelationEnum.COMPATIBILITY,  WeightEnum.PERCENT),

    ;

    /**
     * 与其他优惠券关系
     */
    private CouponsRelationEnum couponsRelation;


    /**
     * 比重类型
     * 注意: 当两张优惠券同时使用, 作用的元素一致而比重类型不一致时, 会抛异常
     */
    private WeightEnum weight;

    StrategyTypeEnum(CouponsRelationEnum couponsRelation,  WeightEnum weight) {
        this.couponsRelation = couponsRelation;
        this.weight = weight;
    }

    public CouponsRelationEnum getCouponsRelation() {
        return couponsRelation;
    }


    public WeightEnum getWeight() {
        return weight;
    }
}
