package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.exception.SchemaException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略计算器实现类
 * Created by xzhang on 2018/9/18.
 */
@Component("strategyCalculator")
public class StrategyCalculatorImpl implements CouponCalculator {

    @Override
    public Schema getCurrentSchema(List<? extends ISchemaEntity> schemaEntities, LoanOrderEntity loanOrder) {
        return null;
    }

    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        throw new SchemaException("策略计算器不支持OriginalSchema");
    }

    private List<StrategyEntity> checkSchemaEntity (List<? extends ISchemaEntity> schemaEntities, String orderId) throws SchemaException {
        if (schemaEntities == null || schemaEntities.size() == 0) {
            throw new SchemaException("优惠券计算器传入实体为空!");
        }
        List<StrategyEntity> strategyEntities = new ArrayList<StrategyEntity>();
        // 检查传入参数是否正确
        for (ISchemaEntity schemaEntity : schemaEntities) {
            // 1. 检查类型
            if (!(schemaEntity instanceof StrategyEntity)) {
                throw new SchemaException("优惠券计算器传入实体并非优惠券! 实体类型为" + schemaEntity.getSchemaType().name());
            }
            StrategyEntity strategyEntity = (StrategyEntity) schemaEntity;

            strategyEntities.add(strategyEntity);
        }
        return strategyEntities;
    }
}
