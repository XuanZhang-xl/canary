package com.xl.canary.entity;

import com.xl.canary.enums.SubjectEnum;

import java.math.BigDecimal;

/**
 * 订单抽象类
 * Created by xzhang on 2018/9/5.
 */
public interface IOrderEntity extends IStateEntity {

    /**
     * 获取金额  借款金额/还款金额
     * @return
     */
    BigDecimal getEquivalentAmount();

    /**
     * 获取币种, 借款币种, 还款币种
     * @return
     */
    String getEquivalent();

    /**
     * 获取用户
     * @return
     */
    String getUserCode();

    /**
     * 获取订单类型
     * @return
     */
    SubjectEnum getOrderSubjectType();
}
