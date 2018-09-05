package com.xl.canary.entity;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.enums.CurrencyEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.loan.LoanOrderTypeEnum;
import com.xl.canary.enums.TimeUnitEnum;
import com.xl.canary.utils.Fee;
import com.xl.canary.utils.IDWorker;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单表
 * created by XUAN on 2018/08/18
 */
@Table(name = "t_canary_loan_order")
public class LoanOrderEntity extends AbstractOrderEntity {

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
     * 借款订单状态
     */
    private String orderState;

    /**
     * 还款日, 只有  某些  orderType   时才有效, 为计算还款日有效
     */
    private Integer repaymentDay;

    /**
     * 分期数
     */
    private Integer instalment;

    /**
     * 分期单位, 年, 月, 周, 日
     */
    private String instalmentUnit;

    /**
     * 分期利率, 年利率, 月利率, 日利率
     */
    private BigDecimal instalmentRate;

    /**
     * 罚息利率
     */
    private BigDecimal penaltyRate;

    /**
     * 放款模式
     */
    private String lendMode;

    /**
     * 借款币种
     */
    private String applyCurrency;

    /**
     * 一般等价物, 锚定币种
     */
    private String equivalent;

    /**
     * 一般等价物的兑换率, 1个applyCurrency = x个equivalent
     */
    private BigDecimal equivalentRate;

    /**
     * 申请借款金额, 对应借款币种
     */
    private BigDecimal applyAmount = BigDecimal.ZERO;

    /**
     * 放款金额, 对应借款币种
     */
    private BigDecimal lentAmount = BigDecimal.ZERO;

    /**
     * 锚定金额, 对应锚定币种
     */
    private BigDecimal equivalentAmount = BigDecimal.ZERO;

    /**
     * 其他费用, 要分配到分期, JSON
     */
    private String fee;

    /**
     * 放款时间
     */
    private Long lentTime = -1L;

    /**
     * 结束时间
     */
    private Long endTime = -1L;

    /**
     * 时区
     */
    private Integer timeZone;

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

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public String getInstalmentUnit() {
        return instalmentUnit;
    }

    public void setInstalmentUnit(String instalmentUnit) {
        this.instalmentUnit = instalmentUnit;
    }

    public BigDecimal getInstalmentRate() {
        return instalmentRate;
    }

    public void setInstalmentRate(BigDecimal instalmentRate) {
        this.instalmentRate = instalmentRate;
    }

    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public String getApplyCurrency() {
        return applyCurrency;
    }

    public void setApplyCurrency(String applyCurrency) {
        this.applyCurrency = applyCurrency;
    }

    @Override
    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getLentAmount() {
        return lentAmount;
    }

    public void setLentAmount(BigDecimal lentAmount) {
        this.lentAmount = lentAmount;
    }

    @Override
    public BigDecimal getEquivalentAmount() {
        return equivalentAmount;
    }

    public void setEquivalentAmount(BigDecimal equivalentAmount) {
        this.equivalentAmount = equivalentAmount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Long getLentTime() {
        return lentTime;
    }

    public void setLentTime(Long lentTime) {
        this.lentTime = lentTime;
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

    public Integer getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(Integer repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public BigDecimal getEquivalentRate() {
        return equivalentRate;
    }

    public void setEquivalentRate(BigDecimal equivalentRate) {
        this.equivalentRate = equivalentRate;
    }

    public String getLendMode() {
        return lendMode;
    }

    public void setLendMode(String lendMode) {
        this.lendMode = lendMode;
    }

    public static LoanOrderEntity foo() {
        LoanOrderEntity loanOrder = new LoanOrderEntity();
        loanOrder.setOrderId(IDWorker.getNewID());
        loanOrder.setUserCode("1");
        loanOrder.setOrderState(StateEnum.PENDING.name());
        loanOrder.setOrderType(LoanOrderTypeEnum.GENERAL_TYPE.name());
        loanOrder.setInstalment(3);
        loanOrder.setInstalmentRate(new BigDecimal("0.025"));
        loanOrder.setInstalmentUnit(TimeUnitEnum.MONTH.name());
        loanOrder.setPenaltyRate(loanOrder.instalmentRate.multiply(new BigDecimal("1.5")));
        loanOrder.setApplyCurrency(CurrencyEnum.USDT.name());
        loanOrder.setApplyAmount(new BigDecimal("100"));
        loanOrder.setEquivalent(loanOrder.getApplyCurrency());
        loanOrder.setEquivalentRate(BigDecimal.ONE);
        loanOrder.setEquivalentAmount(loanOrder.getApplyAmount());
        Long now = System.currentTimeMillis();
        loanOrder.setCreateTime(now);
        loanOrder.setUpdateTime(now);
        loanOrder.setTimeZone(8);
        loanOrder.setIsDeleted(0);
        return loanOrder;
    }

    public List<Fee> getDeserializeFee () {
        if (this.fee == null) {
            return null;
        }
        return JSONObject.parseArray(fee, Fee.class);
    }

    @Override
    public String getUniqueId() {
        return orderId;
    }

    @Override
    public String getState() {
        return orderState;
    }
}
