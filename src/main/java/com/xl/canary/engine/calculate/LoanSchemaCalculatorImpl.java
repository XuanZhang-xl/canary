package com.xl.canary.engine.calculate;

import com.xl.canary.bean.structure.Schema;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 还款订单schema计算器
 * created by XUAN on 2018/09/08
 */
@Component("loanOrderSchemaCalculatorImpl")
public class LoanSchemaCalculatorImpl implements LoanSchemaCalculator {


    @Override
    public Schema getOriginalSchema(String orderId) {
        return null;
    }

    @Override
    public Schema getCurrentSchema(String orderId) {
        return null;
    }

    @Override
    public Schema getPlanSchema(String orderId) {
        return null;
    }

    @Override
    public Map<Integer, Long> repaymentDates(String orderId) {
        return null;
    }
}
