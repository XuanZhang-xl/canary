package com.xl.canary.utils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 分期基础信息
 * Created by xzhang on 2018/8/21.
 */
public class BasicInstalment {

    /**
     * 分期
     */
    private Integer instalment;

    /**
     * 本金
     */
    private BigDecimal principal;

    /**
     * 利息
     */
    private BigDecimal interest;

    /**
     * 罚息
     */
    private BigDecimal penalty;

    /**
     * 不跟随分期费用
     */
    private Map<String, BigDecimal> fee;

    /**
     * 跟随分期费用
     */
    private Map<String, BigDecimal> feeFollowInstalment;

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

    public Map<String, BigDecimal> getFee() {
        return fee;
    }

    public void setFee(Map<String, BigDecimal> fee) {
        this.fee = fee;
    }

    public Map<String, BigDecimal> getFeeFollowInstalment() {
        return feeFollowInstalment;
    }

    public void setFeeFollowInstalment(Map<String, BigDecimal> feeFollowInstalment) {
        this.feeFollowInstalment = feeFollowInstalment;
    }

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
}
