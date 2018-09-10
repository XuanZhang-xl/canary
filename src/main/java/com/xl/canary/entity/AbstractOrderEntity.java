package com.xl.canary.entity;

import java.math.BigDecimal;

/**
 * 订单抽象类
 * Created by xzhang on 2018/9/5.
 */
public abstract class AbstractOrderEntity extends AbstractBaseEntity implements IStateEntity {

    /**
     * 获取金额  借款金额/还款金额
     * @return
     */
    public abstract BigDecimal getEquivalentAmount();

    /**
     * 获取币种, 借款币种, 还款币种
     * @return
     */
    public abstract String getEquivalent();

    /**
     * 获取用户
     * @return
     */
    public abstract String getUserCode();

    /**
     * 获取订单类型
     * @return
     */
    public abstract String getOrderSubjectType();
}
