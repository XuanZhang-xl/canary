package com.xl.canary.entity;

import java.math.BigDecimal;

/**
 * created by XUAN on 2018/08/23
 */
public class PayOrderEntity extends AbstractBaseEntity implements IStateEntity {

    /**
     * 还款订单号
     */
    private String payOrderId;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 状态
     */
    private String payOrderState;

    /**
     * 还款类型
     */
    private String type;
    private Long payTime;
    private BigDecimal payNumber;

    private BigDecimal entryNumber;

    private Long entryOverTime;

    /**
     * 锚定金额, 对应锚定币种
     */
    private BigDecimal equivalentAmount = BigDecimal.ZERO;

    public String getPayOrderId() {
        return payOrderId;
    }


    @Override
    public String getUniqueId() {
        return payOrderId;
    }

    @Override
    public String getState() {
        return null;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getPayOrderState() {
        return payOrderState;
    }

    public void setPayOrderState(String payOrderState) {
        this.payOrderState = payOrderState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setPayNumber(BigDecimal payNumber) {
        this.payNumber = payNumber;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPayNumber() {
        return payNumber;
    }

    public BigDecimal getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(BigDecimal entryNumber) {
        this.entryNumber = entryNumber;
    }

    public BigDecimal getEquivalentAmount() {
        return equivalentAmount;
    }

    public void setEquivalentAmount(BigDecimal equivalentAmount) {
        this.equivalentAmount = equivalentAmount;
    }

    public Long getEntryOverTime() {
        return entryOverTime;
    }

    public void setEntryOverTime(Long entryOverTime) {
        this.entryOverTime = entryOverTime;
    }
}
