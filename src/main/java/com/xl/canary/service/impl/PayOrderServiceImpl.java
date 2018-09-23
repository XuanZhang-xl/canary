package com.xl.canary.service.impl;

import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.mapper.LoanOrderMapper;
import com.xl.canary.mapper.PayOrderMapper;
import com.xl.canary.service.PayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by XUAN on 2018/08/23
 */
@Service("payOrderServiceImpl")
public class PayOrderServiceImpl implements PayOrderService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Override
    public PayOrderEntity getByPayOrderId(String payOrderId) {
        return payOrderMapper.getByPayOrderId(payOrderId);
    }

    @Override
    public PayOrderEntity save(PayOrderEntity payOrder) {
        payOrderMapper.insert(payOrder);
        return payOrder;
    }
}
