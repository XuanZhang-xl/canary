package com.xl.canary.engine.calculate;

import com.xl.canary.bean.structure.Schema;

import java.util.Map;

/**
 * schema计算器
 * created by XUAN on 2018/09/08
 */
public interface SchemaCalculator {

    /**
     * 获取订单的原始账单, 下单时的账单, 一分未还的账单
     * @param orderId   订单号
     * @return
     */
    Schema getOriginalSchema(String orderId);

    /**
     * 获取订单的当前账单, 当前的立马还清的账单
     * @param orderId   订单号
     * @return
     */
    Schema getCurrentSchema(String orderId);

    /**
     * 获取订单的计划账单, 当前时间到订单结束如果不提前还款, 在还款日还清的账单
     * @param orderId   订单号
     * @return
     */
    Schema getPlanSchema(String orderId);

    /**
     * 获取订单的计划账单, 当前时间到订单结束如果不提前还款, 在还款日还清的账单
     * @param orderId   订单号
     * @return
     */
    Map<Integer, Long> repaymentDates (String orderId);

}
