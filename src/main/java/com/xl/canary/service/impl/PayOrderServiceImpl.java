package com.xl.canary.service.impl;

import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.service.PayOrderService;
import org.springframework.stereotype.Service;

/**
 * created by XUAN on 2018/08/23
 */
@Service("payOrderServiceImpl")
public class PayOrderServiceImpl implements PayOrderService {

    @Override
    public PayOrderEntity getByOrderId(String orderId) {
        return null;
    }

    @Override
    public void save(PayOrderEntity entity) {

    }
}
