package com.xl.canary.bean.req;

import com.xl.canary.enums.CurrencyEnum;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * created by XUAN on 2018/08/19
 */
public class PayOrderReq {

    /**
     * 用户编号, 应放在Header里, 这里时为测试方便
     */
    @NotEmpty
    private String userCode;

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
     * 所还的借款订单
     */
    @NotNull
    private String loanOrderId;

    /**
     * 所用的优惠券
     */
    private List<String> couponIds;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

    public String getLoanOrderId() {
        return loanOrderId;
    }

    public void setLoanOrderId(String loanOrderId) {
        this.loanOrderId = loanOrderId;
    }

    public List<String> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<String> couponIds) {
        this.couponIds = couponIds;
    }
}
