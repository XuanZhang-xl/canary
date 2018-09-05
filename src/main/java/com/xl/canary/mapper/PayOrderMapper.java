package com.xl.canary.mapper;

import com.xl.canary.entity.PayOrderEntity;
import tk.mybatis.mapper.common.Mapper;

/**
 * created by XUAN on 2018/08/20
 */
@org.apache.ibatis.annotations.Mapper
public interface PayOrderMapper extends Mapper<PayOrderEntity>{

    /**
     * 根据订单号查询
     * @param payOrderId
     * @return
     */
    PayOrderEntity getByPayOrderId(String payOrderId);
}
