package com.xl.canary.entity;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 优惠券实体
 * Created by xzhang on 2018/9/5.
 */
@Table(name = "t_canary_coupon")
public class CouponEntity extends AbstractBaseEntity implements IStateEntity  {

    /**
     * 唯一编号
     */
    private String couponId;

    /**
     * 类型
     */
    private String couponType;

    /**
     * 状态
     */
    private String couponState;

    /**
     * 用户名
     */
    private String userCode;

    /**
     * 绑定用户编号
     */
    private String boundOrderId;

    /**
     * 使用限制, json字段, CouponConditionEntity的缩小版本
     * 说明能不能用这个优惠券
     */
    private String condition;

    /**
     * 一般等价物
     */
    private String equivalent;

    /**
     * 默认值, 根据类型可能时百分比或固定量, 如果是固定量, 则应与apply_amount相等
     */
    private BigDecimal defaultAmount;

    /**
     * 默认值转化后的值, 可优惠金额
     */
    private BigDecimal applyAmount;

    /**
     * 已使用金额
     */
    private BigDecimal entryAmount;

    /**
     * 生效起始时间, 冗余, 会在condition 中体现
     */
    private Long effectiveDate;

    /**
     * 有效天数
     */
    private Integer effectiveDays;

    /**
     * 开始使用时间
     */
    private Long applyTime;

    /**
     * 结束时间
     */
    private Long endTime;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponState() {
        return couponState;
    }

    public void setCouponState(String couponState) {
        this.couponState = couponState;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBoundOrderId() {
        return boundOrderId;
    }

    public void setBoundOrderId(String boundOrderId) {
        this.boundOrderId = boundOrderId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getEquivalent() {
        return equivalent;
    }

    public void setEquivalent(String equivalent) {
        this.equivalent = equivalent;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public BigDecimal getApplyAmount() {
        return applyAmount;
    }

    public void setApplyAmount(BigDecimal applyAmount) {
        this.applyAmount = applyAmount;
    }

    public BigDecimal getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(BigDecimal entryAmount) {
        this.entryAmount = entryAmount;
    }

    public Long getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Long effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Integer getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Integer effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getUniqueId() {
        return couponId;
    }

    @Override
    public String getState() {
        return couponState;
    }
}
