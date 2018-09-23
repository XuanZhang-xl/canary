package com.xl.canary.entity;

import com.xl.canary.enums.SchemaTypeEnum;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 优惠券实体, 支持惩罚券
 * Created by xzhang on 2018/9/5.
 */
@Table(name = "t_canary_coupon")
public class CouponEntity extends AbstractConditionEntity implements IStateEntity, ISchemaEntity {

    /**
     * 批量优惠券编号, 有多张优惠券组成, 一个couponBatchId下的所有优惠券合成一个优惠券schema
     */
    private String couponBatchId;

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
     * 用户名, 有值表示已绑定用户, 入账完成必定有值
     * condition中体现
     */
    private String userCode;

    /**
     * 有值表示已绑定订单, 不事先绑定, 入账完成也不一定有值, 因为可能入了多个订单
     * TODO: 绑定后, 逻辑似乎有点复杂
     * condition中体现
     */
    private String boundOrderId;

    /**
     * 辅助字段, 用于计算schema, 决定优惠某一期, 空表示都有效
     */
    private Integer instalment;

    /**
     * 辅助字段, 用于计算schema, 决定优惠某一元素, 空表示都有效
     */
    private String element;

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
     * 生效起始时间, 本来这两限制可以加在condition 中的
     * 但是由于这两参数经常用于显示, 且所有优惠券都会有, 而写在condition中不方便取, 所以另立字段
     */
    private Long effectiveDate;

    /**
     * 失效日期
     */
    private Long expireDate;

    /**
     * 已入账的次数,
     */
    private Integer enteredFrequency;

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

    @Override
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

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
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

    public String getCouponBatchId() {
        return couponBatchId;
    }

    public void setCouponBatchId(String couponBatchId) {
        this.couponBatchId = couponBatchId;
    }

    public Integer getInstalment() {
        return instalment;
    }

    public void setInstalment(Integer instalment) {
        this.instalment = instalment;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Integer getEnteredFrequency() {
        return enteredFrequency;
    }

    public void setEnteredFrequency(Integer enteredFrequency) {
        this.enteredFrequency = enteredFrequency;
    }

    @Override
    public String getUniqueId() {
        return couponId;
    }

    @Override
    public String getState() {
        return couponState;
    }

    @Override
    public SchemaTypeEnum getSchemaType() {
        return SchemaTypeEnum.COUPON;
    }
}
