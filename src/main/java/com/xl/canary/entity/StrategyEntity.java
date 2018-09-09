package com.xl.canary.entity;

import javax.persistence.Table;

/**
 * 策略entity
 * created by XUAN on 2018/09/09
 */
@Table(name = "t_canary_strategy")
public class StrategyEntity extends AbstractBaseEntity {

    /**
     * 策略id
     */
    private String strategyId;

    /**
     * 策略类型
     */
    private String strategyType;

    /**
     * 限制条件, 可能时List
     */
    private String condition;

    /**
     * 起效时间, -1为一直有效, 包括这个时间(包头)
     */
    private Long effectiveDate;

    /**
     * 失效时间, -1为一直有效, 不包括这个时间(不包尾)
     */
    private Long expireDate;

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }
}
