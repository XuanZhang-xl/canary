package com.xl.canary.entity;


import com.xl.canary.enums.BillTypeEnum;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单表
 * created by XUAN on 2018/08/06
 */
@Table(name = "t_canary_instalment")
public class LoanInstalmentEntity extends AbstractBaseEntity implements IStateEntity, ISchemaEntity {

    /**
     * 分期号
     */
    private String instalmentId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户编号
     */
    private String userCode;

    /**
     * 借款订单类型
     */
    private String orderType;

    /**
     * 分期订单状态
     */
    private String instalmentState;

    /**
     * 分期数, 第几期
     */
    private Integer instalment;

    /**
     * 一般等价物, 锚定币种
     */
    private String equivalent;

    /**
     * 原始本金
     */
    private BigDecimal originalPrincipal = BigDecimal.ZERO;

    /**
     * 原始利息
     */
    private BigDecimal originalInterest = BigDecimal.ZERO;

    /**
     * 原始其他费用, JSON
     */
    private String originalFee;

    /**
     * 应还本金
     */
    private BigDecimal principal = BigDecimal.ZERO;

    /**
     * 应还利息
     */
    private BigDecimal interest = BigDecimal.ZERO;

    /**
     * 应还罚息
     */
    private BigDecimal penalty = BigDecimal.ZERO;

    /**
     * 应还其他费用, JSON
     */
    private String fee;

    /**
     * 应还清时间
     */
    private Long shouldPayTime = -1L;

    /**
     * 超过这个时间, 此分期强制结束, 如果为 -1 , 表示会一直记罚息
     */
    private Long clearTime = -1L;

    /**
     * 实际结束时间
     */
    private Long endTime = -1L;

    /**
     * 时区
     */
    private Integer timeZone;

    @Override
    public String getUniqueId() {
        return instalmentId;
    }

    @Override
    public String getState() {
        return instalmentState;
    }

    public String getInstalmentId() {
        return instalmentId;
    }

    public void setInstalmentId(String instalmentId) {
        this.instalmentId = instalmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getInstalmentState() {
        return instalmentState;
    }

    public void setInstalmentState(String instalmentState) {
        this.instalmentState = instalmentState;
    }

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public BigDecimal getOriginalPrincipal() {
        return originalPrincipal;
    }

    public void setOriginalPrincipal(BigDecimal originalPrincipal) {
        this.originalPrincipal = originalPrincipal;
    }

    public BigDecimal getOriginalInterest() {
        return originalInterest;
    }

    public void setOriginalInterest(BigDecimal originalInterest) {
        this.originalInterest = originalInterest;
    }

    public String getOriginalFee() {
        return originalFee;
    }

    public void setOriginalFee(String originalFee) {
        this.originalFee = originalFee;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Long getShouldPayTime() {
        return shouldPayTime;
    }

    public void setShouldPayTime(Long shouldPayTime) {
        this.shouldPayTime = shouldPayTime;
    }

    public Long getClearTime() {
        return clearTime;
    }

    public void setClearTime(Long clearTime) {
        this.clearTime = clearTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public BillTypeEnum getSchemaType() {
        return BillTypeEnum.LOAN_ORDER;
    }
}
