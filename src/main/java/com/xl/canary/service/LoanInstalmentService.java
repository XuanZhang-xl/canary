package com.xl.canary.service;

import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;

import java.util.List;

/**
 * created by XUAN on 2018/08/21
 */
public interface LoanInstalmentService {

    /**
     * 保存
     * @param loanInstalments
     */
    void saveLoanInstalments(List<LoanInstalmentEntity> loanInstalments);

    /**
     * 保存
     * @param instalment
     */
    void update(LoanInstalmentEntity instalment);

    /**
     * 根据订单计算分期
     * @param loanOrder
     * @return
     */
    List<LoanInstalmentEntity> generateInstalments(LoanOrderEntity loanOrder);

    /**
     * 根据借款订单号查询分期
     * @param orderId
     * @return
     */
    List<LoanInstalmentEntity> listInstalments(String orderId);
}
