package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import org.springframework.stereotype.Component;

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
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }
}
