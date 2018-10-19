package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.SchemaCalculator;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.PayOrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 借款订单原始schema计算器
 * TODO: 貌似不需要
 * created by XUAN on 2018/09/08
 */
@Component("loanOrderSchemaCalculatorImpl")
public class LoanSchemaCalculatorImpl implements SchemaCalculator {

    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) {
        return null;
    }
}
