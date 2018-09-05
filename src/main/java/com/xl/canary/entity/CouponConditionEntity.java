package com.xl.canary.entity;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;

import javax.persistence.Table;

/**
 * 以List形式限制优惠券的使用
 * TODO: 如果遇到复杂情况只是这样的判断可能不行, 到时候可以设计一个判断条件的接口, 每一个优惠券类型都要有一个实现
 * created by XUAN on 2018/09/04
 */
@Table(name = "t_canary_coupon_condition_set")
public class CouponConditionEntity extends AbstractBaseEntity {

    /**
     * 优惠券类型
     */
    private CouponTypeEnum couponType;

    /**
     * 条件
     */
    private CouponConditionEnum condition;

    /**
     * 操作符
     */
    private ArithmeticOperatorEnum operator;

    /**
     * 值
     */
    private String value;

    public CouponTypeEnum getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponTypeEnum couponType) {
        this.couponType = couponType;
    }

    public CouponConditionEnum getCondition() {
        return condition;
    }

    public void setCondition(CouponConditionEnum condition) {
        this.condition = condition;
    }

    public ArithmeticOperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(ArithmeticOperatorEnum operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
