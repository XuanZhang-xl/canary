package com.xl.canary.engine.calculate;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.PayOrderEntity;

import java.util.List;

/**
 * 优惠券计算器
 * Created by xzhang on 2018/9/18.
 */
public interface CouponCalculator extends SchemaCalculator {

    /**
     * 获取订单的当前账单, 当前的立马还清的账单
     * @param schemaEntities   借款订单
     * @param payOrder         还款订单, 用于检查条件
     * @return   schema
     */
    Schema getCurrentSchema(List<? extends ISchemaEntity> schemaEntities, PayOrderEntity payOrder);
}
