package com.xl.canary.entity;

import com.xl.canary.enums.SchemaTypeEnum;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 策略entity
 * 单独列策略的原因:
 * 1. 策略是可以一直存在的, 可以一直用于入账, 而优惠券是有额度的
 * 2. 策略是无状态的, 或者说只有有效无效两个状态, 是系统级的,  而优惠券是需要分配的, 给某个人或某订单
 * created by XUAN on 2018/09/09
 */
@Table(name = "t_canary_strategy")
public class StrategyEntity extends AbstractConditionEntity implements ISchemaEntity {

    /**
     * 策略id
     */
    private String strategyId;

    /**
     * 策略类型
     */
    private String strategyType;

    /**
     * 辅助字段, 用于计算schema, 决定优惠某一期, 空表示都有效
     */
    private Integer instalment;

    /**
     * 辅助字段, 用于计算schema, 决定优惠某一元素, 空表示都有效
     */
    private String element;

    /**
     * 限制条件, 可能时List
     * 包括了subject的限制
     */
    private String condition;

    /**
     * 一般等价物
     */
    private String equivalent;

    /**
     * 默认值
     */
    private BigDecimal defaultAmount;

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

    @Override
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
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

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    @Override
    public SchemaTypeEnum getSchemaType() {
        return SchemaTypeEnum.STRATEGY;
    }
}
