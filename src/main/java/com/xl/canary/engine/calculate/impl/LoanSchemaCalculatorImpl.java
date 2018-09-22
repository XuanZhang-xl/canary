package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.LoanSchemaCalculator;
import com.xl.canary.entity.ISchemaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 还款订单schema计算器
 * created by XUAN on 2018/09/08
 */
@Component("loanOrderSchemaCalculatorImpl")
public class LoanSchemaCalculatorImpl implements LoanSchemaCalculator {


    @Override
    public Schema getCurrentSchema(Long date, List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }

    @Override
    public Schema getPlanSchema(Long date, List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }

    @Override
    public Map<Integer, Long> repaymentDates(List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }

    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }
}
