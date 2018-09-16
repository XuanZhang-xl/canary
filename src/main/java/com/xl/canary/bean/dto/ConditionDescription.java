package com.xl.canary.bean.dto;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;

/**
 * 以List形式限制优惠券的使用
 * [
 * {
 *     "condition":"ENTRY_FREQUENCY",
 *     "standardParam":"1",
 *     "operator":"LESS_THAN_OR_EQUAL",
 *     "currentParam":"${ENTRY_FREQUENCY}"
 * },{
 *     "condition":"INSTALMENT",
 *     "standardParam":"1",
 *     "operator":"IN",
 *     "currentParam":"${INSTALMENT}"
 * },{
 *     "condition":"PAY_CURRENCY",
 *     "standardParam":"${LOAN_CURRENCY}",
 *     "operator":"NOT_EQUAL",
 *     "currentParam":"${PAY_CURRENCY}"
 * }
 * ]
 * created by XUAN on 2018/09/04
 */
public class ConditionDescription {

    /**
     * 条件类型
     */
    private CouponConditionEnum condition;

    /**
     * 标准值, 基准值, 可以固定, 可以动态获取
     */
    private String standardParam;

    /**
     * 操作符
     */
    private ArithmeticOperatorEnum operator;

    /**
     * 当前的值, 永远动态获取
     */
    private String currentParam;

    public ConditionDescription(CouponConditionEnum condition, ArithmeticOperatorEnum operator, String value) {
        this.condition = condition;
        this.operator = operator;
        this.currentParam = value;
    }

    public CouponConditionEnum getCondition() {
        return condition;
    }

    public void setCondition(CouponConditionEnum condition) {
        this.condition = condition;
    }

    public String getStandardParam() {
        return standardParam;
    }

    public void setStandardParam(String standardParam) {
        this.standardParam = standardParam;
    }

    public ArithmeticOperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(ArithmeticOperatorEnum operator) {
        this.operator = operator;
    }

    public String getCurrentParam() {
        return currentParam;
    }

    public void setCurrentParam(String currentParam) {
        this.currentParam = currentParam;
    }
}
