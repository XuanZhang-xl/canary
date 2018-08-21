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
     * @param loanInstalment
     */
    void save(LoanInstalmentEntity loanInstalment);

    /**
     * 根据订单获得分期
     * @param loanOrder
     * @return
     */
    List<LoanInstalmentEntity> generateInstalments(LoanOrderEntity loanOrder);
}
