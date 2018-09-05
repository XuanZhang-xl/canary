package com.xl.canary.bean.req;

import com.xl.canary.enums.CurrencyEnum;
import com.xl.canary.enums.loan.LoanOrderTypeEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * created by XUAN on 2018/08/19
 */
public class LoanOrderReq {

    /**
     * 用户编号, 应放在Header里, 这里时为测试方便
     */
    @NotEmpty
    private String userCode;

    /**
     * 订单类型
     */
    @NotNull
    private LoanOrderTypeEnum loanOrderType;

    /**
     * 借款币种
     */
    @NotNull
    private CurrencyEnum applyCurrency;

    /**
     * 借款金额
     */
    @Min(0)
    private BigDecimal amount;

    /**
     * 分期期数
     */
    @Min(0)
    private Integer instalment;

    /**
     * 时区, 单位为分钟
     */
    @NotNull
    private Integer timeZone;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public LoanOrderTypeEnum getLoanOrderType() {
        return loanOrderType;
    }

    public void setLoanOrderType(LoanOrderTypeEnum loanOrderType) {
        this.loanOrderType = loanOrderType;
    }

    public CurrencyEnum getApplyCurrency() {
        return applyCurrency;
    }

    public void setApplyCurrency(CurrencyEnum applyCurrency) {
        this.applyCurrency = applyCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public Integer getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Integer timeZone) {
        this.timeZone = timeZone;
    }
}
