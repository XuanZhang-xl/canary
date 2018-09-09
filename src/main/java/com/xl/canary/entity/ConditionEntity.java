package com.xl.canary.entity;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;

import javax.persistence.Table;

/**
 * 以List形式限制优惠券的使用
 *
 * 写限制条件时请注意: 每条限制条件之间的关系都为'&&'
 * created by XUAN on 2018/09/04
 */
@Table(name = "t_canary_condition_set")
public class ConditionEntity extends AbstractBaseEntity {

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
    private ArithmeticOperatorEnum operator = ArithmeticOperatorEnum.SPECIAL;

    /**
     * 目标值, 可能为数组
     */
    private String target;

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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget (LoanOrderEntity loanOrder, PayOrderEntity payOrder) {
        // TODO: 以后的变态需求就在这儿写
        if (CouponConditionEnum.SPECIAL.equals(condition)) {
            return payOrder.getApplyCurrency();
        } else {
            return target;
        }
    }
}
