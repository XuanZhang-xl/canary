package com.xl.canary.service;

import com.xl.canary.entity.PayOrderEntity;

/**
 * created by XUAN on 2018/08/23
 */
public interface PayOrderService {

    /**
     * 获取还款订单
     * @param orderId
     * @return
     */
    PayOrderEntity getByOrderId(String orderId);

    /**
     * 保存订单
     * @param entity
     */
    void save(PayOrderEntity entity);
}
