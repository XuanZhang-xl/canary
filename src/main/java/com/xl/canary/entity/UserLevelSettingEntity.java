package com.xl.canary.entity;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 用户级别表
 * created by XUAN on 2018/08/20
 */
@Table(name = "t_canary_user_level_setting")
public class UserLevelSettingEntity extends AbstractBaseEntity{

    /**
     * 级别
     */
    private String level;

    /**
     * 日利率
     */
    private BigDecimal dailyInterestRate;

    /**
     * 日罚息率
     */
    private BigDecimal dailyPenaltyRate;

    /**
     * 累计最大借款额
     */
    private BigDecimal maxLoanableAmount;

    /**
     * 每次最小借款金额
     */
    private BigDecimal minLoanableAmount;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getDailyInterestRate() {
        return dailyInterestRate;
    }

    public void setDailyInterestRate(BigDecimal dailyInterestRate) {
        this.dailyInterestRate = dailyInterestRate;
    }

    public BigDecimal getDailyPenaltyRate() {
        return dailyPenaltyRate;
    }

    public void setDailyPenaltyRate(BigDecimal dailyPenaltyRate) {
        this.dailyPenaltyRate = dailyPenaltyRate;
    }

    public BigDecimal getMaxLoanableAmount() {
        return maxLoanableAmount;
    }

    public void setMaxLoanableAmount(BigDecimal maxLoanableAmount) {
        this.maxLoanableAmount = maxLoanableAmount;
    }

    public BigDecimal getMinLoanableAmount() {
        return minLoanableAmount;
    }

    public void setMinLoanableAmount(BigDecimal minLoanableAmount) {
        this.minLoanableAmount = minLoanableAmount;
    }
}
