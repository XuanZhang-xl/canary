package com.xl.canary.bean.dto;

import com.xl.canary.entity.AbstractBaseEntity;
import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;

import javax.persistence.Table;

/**
 * 以List形式限制优惠券的使用
 * created by XUAN on 2018/09/04
 */
public class CouponCondition {

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

    public CouponCondition(CouponConditionEnum condition, ArithmeticOperatorEnum operator, String value) {
        this.condition = condition;
        this.operator = operator;
        this.value = value;
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
