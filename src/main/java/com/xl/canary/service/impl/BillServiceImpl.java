package com.xl.canary.service.impl;

import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.bean.structure.Unit;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.engine.calculate.impl.InstalmentCalculatorImpl;
import com.xl.canary.engine.calculate.impl.StrategyCalculatorImpl;
import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.engine.calculate.siuation.SituationHolder;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.exception.CouponException;
import com.xl.canary.exception.SchemaException;
import com.xl.canary.service.BillService;
import com.xl.canary.service.CouponService;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * created by XUAN on 2018/09/22
 */
@Service("billServiceImpl")
public class BillServiceImpl implements BillService {

    /**
     * TODO: 使用factory
     */
    @Autowired
    private InstalmentCalculatorImpl instalmentCalculator;

    @Autowired
    private StrategyCalculatorImpl strategyCalculator;

    @Autowired
    private CouponCalculator couponCalculator;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private LoanInstalmentService instalmentService;

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder) {
        // 订单schema
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        return instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) throws BaseException {
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
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());

        Schema loanSchema = instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);

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

    private Schema getRepaySchema(List<Schema> schemas) throws SchemaException {
        // 应还schema
        Schema repaySchema = new Schema();
        Schema sourceSchema = getWriteOffTypeSchema(schemas, SchemaTypeEnum.WRITE_OFF_SOURCE);
        Schema destinationSchema = getWriteOffTypeSchema(schemas, SchemaTypeEnum.WRITE_OFF_DESTINATION);

        // 分配应还schema
        for (Map.Entry<Integer, Instalment> instalmentEntry : destinationSchema.entrySet()) {
            Integer instalmentKey = instalmentEntry.getKey();
            Instalment instalment = instalmentEntry.getValue();
            Instalment sourceInstalment = sourceSchema.get(instalmentKey);
            if (sourceInstalment == null) {
                // 来源为空, 表示暂时没还, 需要还
                repaySchema.put(instalmentKey, instalment);
                continue;
            }
            Instalment repayInstalment = repaySchema.get(instalmentKey);
            if (repayInstalment == null) {
                repayInstalment = new Instalment();
                repaySchema.put(instalmentKey, repayInstalment);
            }
            // instalment 中有一部分还了
            for (Map.Entry<LoanOrderElementEnum, Unit> unitEntry : instalment.entrySet()) {
                LoanOrderElementEnum elementKey = unitEntry.getKey();
                Unit unit = unitEntry.getValue();
                Unit sourceUnit = sourceInstalment.get(elementKey);
                if (sourceUnit == null) {
                    // 来源为空, 表示暂时没还, 需要还
                    repayInstalment.put(elementKey, unit);
                    repaySchema.put(instalmentKey, repayInstalment);
                    continue;
                }
                Unit repayUnit = repayInstalment.get(elementKey);
                if (repayUnit == null) {
                    repayUnit = new Unit();
                    repayInstalment.put(elementKey, repayUnit);
                }
                // unit中有部分还了
                Iterator<Element> iterator = sourceUnit.iterator();
                Element sourceElement = null;
                for (Element element : unit) {
                    BigDecimal amount = element.getAmount();
                    if (iterator.hasNext()) {
                        sourceElement = iterator.next();
                        BigDecimal sourceAmount = sourceElement.getAmount();
                        if (amount.compareTo(sourceAmount) > 0) {
                            // 不够还, 减掉后放入应还schema
                            Element repayElement = element.clone();
                            repayElement.setAmount(amount.subtract(sourceAmount));
                            repayUnit.add(repayElement);
                        } else {
                            // 还完了, do nothing
                        }
                    } else {
                        // 已还的匹配完, 剩下的全是没还的
                        repayUnit.add(element.clone());
                    }
                }
            }
        }
        return repaySchema;
    }

    private Schema getWriteOffTypeSchema(List<Schema> schemas, SchemaTypeEnum writeOffSource) throws SchemaException {
        Schema writeOffSchema = new Schema(writeOffSource);
        for (Schema schema : schemas) {
            for (Map.Entry<Integer, Instalment> instalmentEntry : schema.entrySet()) {
                Integer instalmentKey = instalmentEntry.getKey();
                Instalment instalment = instalmentEntry.getValue();
                Instalment writeOffInstalment = writeOffSchema.get(instalmentKey);
                if (writeOffInstalment == null) {
                    writeOffInstalment = new Instalment();
                }
                for (Map.Entry<LoanOrderElementEnum, Unit> unitEntry : instalment.entrySet()) {
                    LoanOrderElementEnum elementKey = unitEntry.getKey();
                    Unit unit = unitEntry.getValue();
                    Unit writeOffElements = writeOffInstalment.get(elementKey);
                    if (writeOffElements == null) {
                        writeOffElements = new Unit();
                    }
                    for (Element element : unit) {
                        BigDecimal amount = element.getAmount();
                        if (SchemaTypeEnum.WRITE_OFF_DESTINATION.equals(writeOffSource) && BigDecimal.ZERO.compareTo(amount) < 0) {
                            // 数字是正的在这儿
                            writeOffElements.add(element);
                            writeOffInstalment.put(elementKey, writeOffElements);
                            writeOffSchema.put(instalmentKey, writeOffInstalment);
                        } else if (SchemaTypeEnum.WRITE_OFF_SOURCE.equals(writeOffSource) && BigDecimal.ZERO.compareTo(amount) > 0) {
                            // 数字是负的在这儿
                            writeOffElements.add(element);
                            writeOffInstalment.put(elementKey, writeOffElements);
                            writeOffSchema.put(instalmentKey, writeOffInstalment);
                        } else {
                            throw new SchemaException("计算销账schema时的SchemaType为" + writeOffSource.name() + ", 不符合要求!");
                        }
                    }
                }
            }
        }
        return writeOffSchema;
    }
}
