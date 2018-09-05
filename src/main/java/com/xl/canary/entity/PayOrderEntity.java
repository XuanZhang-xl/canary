package com.xl.canary.entity;

import java.math.BigDecimal;

/**
 * created by XUAN on 2018/08/23
 */
public class PayOrderEntity extends AbstractOrderEntity {

    /**
     * 还款订单号
     */
    private String payOrderId;

    /**
     * todo
     */
    private String loanOrderIds;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 类型
     */
    private String payOrderType;

    /**
     * 状态
     */
    private String payOrderState;

    /**
     * 还款币种
     */
    private String applyCurrency;

    /**
     * 申请金额
     */
    private BigDecimal applyAmount;

    /**
     * 实际扣款金额
     */
    private BigDecimal payNumber;

    /**
     * 实际扣款时间
     */
    private Long payTime;

    /**
     * 等价物
     */
    private String equivalent;

    /**
     * 等价物的兑换率
     */
    private BigDecimal equivalentRate;

    /**
     * 准备入账金额
     */
    private BigDecimal equivalentAmount;

    /**
     * 入账金额
     */
    private BigDecimal entryNumber;

    /**
     * 入账结束时间
     */
    private Long entryOverTime;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getLoanOrderIds() {
        return loanOrderIds;
    }

    public void setLoanOrderIds(String loanOrderIds) {
        this.loanOrderIds = loanOrderIds;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public String getPayOrderState() {
        return payOrderState;
    }

    public void setPayOrderState(String payOrderState) {
        this.payOrderState = payOrderState;
    }

    public String getApplyCurrency() {
        return applyCurrency;
    }

    public void setApplyCurrency(String applyCurrency) {
        this.applyCurrency = applyCurrency;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(BigDecimal payNumber) {
        this.payNumber = payNumber;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    @Override
    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public BigDecimal getEquivalentRate() {
        return equivalentRate;
    }

    public void setEquivalentRate(BigDecimal equivalentRate) {
        this.equivalentRate = equivalentRate;
    }

    @Override
    public BigDecimal getEquivalentAmount() {
        return equivalentAmount;
    }

    public void setEquivalentAmount(BigDecimal equivalentAmount) {
        this.equivalentAmount = equivalentAmount;
    }

    public BigDecimal getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(BigDecimal entryNumber) {
        this.entryNumber = entryNumber;
    }

    public Long getEntryOverTime() {
        return entryOverTime;
    }

    public void setEntryOverTime(Long entryOverTime) {
        this.entryOverTime = entryOverTime;
    }

    @Override
    public String getUniqueId() {
        return payOrderId;
    }

    @Override
    public String getState() {
        return payOrderState;
    }
}
