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
    PayOrderEntity getByPayOrderId(String orderId);

    /**
     * 保存
     * @param payOrder   要保存的订单
     * @return           保存后的订单
     */
    PayOrderEntity save(PayOrderEntity payOrder);
}
