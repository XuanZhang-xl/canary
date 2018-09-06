package com.xl.canary.entity;

import javax.persistence.Table;

/**
 * 还款详情, 包括了还款. 策略. 优惠券
 * Created by xzhang on 2018/9/6.
 */
@Table(name = "t_canary_pay_order_detail")
public class PayOrderDetailEntity {

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源id
     */
    private String sourceId;

    /**
     * 目标
     */
    private String destination;

    /**
     * 目标id
     */
    private String destinationId;

    /**
     * 用户id
     */
    private String userCode;

    /**
     * 分期
     */
    private String instalment;

    /**
     * 还款日
     */
    private String repaymentDate;

    /**
     * 等价物
     */
    private String equivalent;

    /**
     * 销帐元素
     */
    private String element;

    /**
     * 还款时应还
     */
    private String shouldPay;

    /**
     * 还款金额
     */
    private String paid;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getInstalment() {
        return instalment;
    }

    public void setInstalment(String instalment) {
        this.instalment = instalment;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(String shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }
}
