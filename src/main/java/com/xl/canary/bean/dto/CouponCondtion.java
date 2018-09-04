package com.xl.canary.bean.dto;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.CouponConditionEnum;

/**
 * 以List形式限制优惠券的使用
 * TODO: 如果遇到复杂情况只是这样的判断可能不行, 到时候可以设计一个判断条件的接口, 每一个优惠券类型都要有一个实现
 * created by XUAN on 2018/09/04
 */
public class CouponCondtion {

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
    private Object value;


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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
