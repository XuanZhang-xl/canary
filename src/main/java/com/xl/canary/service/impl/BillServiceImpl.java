package com.xl.canary.service.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.impl.LoanSchemaCalculatorImpl;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.service.BillService;
import com.xl.canary.service.CouponService;
import com.xl.canary.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by XUAN on 2018/09/22
 */
@Service("billServiceImpl")
public class BillServiceImpl implements BillService {

    /**
     * TODO: 使用factory
     */
    @Autowired
    private LoanSchemaCalculatorImpl loanSchemaCalculator;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private CouponService couponService;

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder) {
        return loanSchemaCalculator.getCurrentSchema(System.currentTimeMillis(), Arrays.asList(loanOrder));
    }

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) {
        // strategyService.listStrategies();


        return null;
    }

    @Override
    public Schema payLoanOrder(LoanOrderEntity loanOrder, BigDecimal payAmount) {
        return null;
    }

    @Override
    public Schema payLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities, BigDecimal payAmount) {
        return null;
    }
}
