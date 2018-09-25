package com.xl.canary.service;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.PayOrderDetailEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.exception.SchemaException;

import java.util.List;

/**
 * 还款明细service
 * Created by xzhang on 2018/9/25.
 */
public interface PayOrderDetailService {

    /**
     * 保存明细
     * @param payOrder         还款订单
     * @param orderSchema      借款schema
     * @param paySchema        还款schema
     * @param couponSchema     优惠券schema
     * @param strategySchema   策略schema
     */
    void saveScheme(PayOrderEntity payOrder, Schema orderSchema, Schema paySchema, Schema couponSchema, Schema strategySchema);

    /**
     * 根据还款订单号查询还款详情
     * @param payOrderId       还款订单号
     * @return                 还款详情
     */
    List<PayOrderDetailEntity> getByPayOrderId(String payOrderId);

    /**
     * 根据借款订单号查询还款详情
     * @param orderId   借款订单号
     * @return                  还款详情
     */
    List<PayOrderDetailEntity> getByOrderId(String orderId);

    /**
     * 还原借款订单的还款schema或优惠券schema
     * @param orderId   借款订单号
     * @param billType  类型
     * @return  schema
     * @throws SchemaException 不支持还原
     */
    Schema recoverSchemaByOrderId(String orderId, BillTypeEnum billType) throws SchemaException;

    /**
     * 还原这个还款订单的schema
     * @param payOrderId     借款订单号
     * @return schema
     * @throws SchemaException 不支持还原
     */
    Schema recoverSchemaByPayOrderId(String payOrderId) throws SchemaException;
}
