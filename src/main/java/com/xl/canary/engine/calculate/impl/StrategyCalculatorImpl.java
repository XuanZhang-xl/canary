package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.PayOrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 策略计算器实现类
 * Created by xzhang on 2018/9/18.
 */
@Component("strategyCalculatorImpl")
public class StrategyCalculatorImpl implements CouponCalculator {

    @Override
    public Schema getCurrentSchema(List<ISchemaEntity> schemaEntities, PayOrderEntity payOrder) {
        return null;
    }

    @Override
    public Schema getOriginalSchema(List<ISchemaEntity> schemaEntities) {
        return null;
    }
}
