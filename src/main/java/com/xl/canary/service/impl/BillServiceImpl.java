package com.xl.canary.service.impl;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.engine.calculate.impl.LoanSchemaCalculatorImpl;
import com.xl.canary.engine.calculate.impl.StrategyCalculatorImpl;
import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.engine.calculate.siuation.SituationHolder;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.exception.CompareException;
import com.xl.canary.exception.CouponException;
import com.xl.canary.exception.NotExistException;
import com.xl.canary.service.BillService;
import com.xl.canary.service.CouponService;
import com.xl.canary.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private StrategyCalculatorImpl strategyCalculator;

    @Autowired
    private CouponCalculator couponCalculator;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private CouponService couponService;

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder) {
        return loanSchemaCalculator.getCurrentSchema(System.currentTimeMillis(), Arrays.asList(loanOrder));
    }

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) throws NotExistException, CompareException, CouponException {
        Situation situation = SituationHolder.getSituation();
        List<StrategyEntity> strategyEntities = strategyService.listStrategies(situation);

        // 策略schema
        Schema strategySchema = strategyCalculator.getCurrentSchema(strategyEntities, loanOrder);

        // 优惠券schema
        Boolean isPassed = couponService.checkCoupons(couponEntities);
        if (!isPassed) {
            throw new CouponException("优惠券集合中含有不可用优惠券");
        }
        Schema couponSchema = couponCalculator.getCurrentSchema(couponEntities, loanOrder);

        // 订单schema
        Schema loanSchema = loanSchemaCalculator.getCurrentSchema(System.currentTimeMillis(), Arrays.asList(loanOrder));

        return this.getRepaySchema(Arrays.asList(loanSchema, couponSchema, strategySchema));
    }

    @Override
    public Schema payLoanOrder(LoanOrderEntity loanOrder, BigDecimal payAmount) {
        return null;
    }

    @Override
    public Schema payLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities, BigDecimal payAmount) {
        return null;
    }

    private Schema getRepaySchema(List<Schema> schemas) {
        // 应还schema
        Schema repaySchema = new Schema();

    }
}
