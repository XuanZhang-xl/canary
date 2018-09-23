package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.LoanSchemaCalculator;
import com.xl.canary.entity.ISchemaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 分期schema计算器
 * created by XUAN on 2018/09/08
 */
@Component("instalmentCalculatorImpl")
public class InstalmentCalculatorImpl implements LoanSchemaCalculator {

    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }

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
}
