package com.xl.canary.service;

import com.xl.canary.bean.condition.LoanOrderCondition;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;

import java.util.List;

/**
 * created by XUAN on 2018/08/07
 */
public interface LoanOrderService {

    /**
     * 保存
     * @param loanOrder
     */
    public LoanOrderEntity save(LoanOrderEntity loanOrder);

    /**
     * 条件查询
     * @param condition
     * @return
     */
    List<LoanOrderEntity> fetchLoanOrders(LoanOrderCondition condition);

    /**
     * 根据订单号获取订单
     * @param orderId
     * @return
     */
    LoanOrderEntity getByOrderId(String orderId);

    /**
     * 更新订单
     * @param entity
     */
    void update(LoanOrderEntity entity);
}