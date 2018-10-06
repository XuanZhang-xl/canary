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
import com.xl.canary.entity.*;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.enums.loan.LoanOrderElementTypeEnum;
import com.xl.canary.enums.pay.EntrySequenceEnum;
import com.xl.canary.exception.*;
import com.xl.canary.service.BillService;
import com.xl.canary.service.CouponService;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.service.StrategyService;
import com.xl.canary.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * created by XUAN on 2018/09/22
 */
@Service("billServiceImpl")
public class BillServiceImpl implements BillService {

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
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder) throws SchemaException {
        // 订单schema
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        return instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    public Schema payOffLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException {
        return this.mergeSchemas(this.loanOrderCouponSchemas(loanOrder), SchemaTypeEnum.SHOULD_PAY);
    }

    @Override
    public Schema payOffAll(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) throws BaseException {
        return this.mergeSchemas(this.loanOrderStrategyCouponSchemas(loanOrder, couponEntities), SchemaTypeEnum.SHOULD_PAY);
    }

    @Override
    public Schema shouldPayLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException {
        Schema schema = this.mergeSchemas(this.loanOrderCouponSchemas(loanOrder), SchemaTypeEnum.SHOULD_PAY);
        // 去除还没到期的期数
        Iterator<Map.Entry<Integer, Instalment>> iterator = schema.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Instalment> instalmentEntry = iterator.next();
            Instalment instalment = instalmentEntry.getValue();
            Long today = TimeUtils.truncateToDay(System.currentTimeMillis());
            if (today < instalment.getRepaymentDate()) {
                iterator.remove();
            }
        }
        return schema;
    }

    @Override
    public Schema planLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException {
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        return instalmentCalculator.getPlanSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    public Schema payLoanOrder(LoanOrderEntity loanOrder, PayOrderEntity payOrder) throws BaseException {
        Schema shouldPaySchemaForEntry = this.mergeSchemas(this.loanOrderCouponSchemas(loanOrder), SchemaTypeEnum.ENTRY);
        return this.entrySchema(shouldPaySchemaForEntry, payOrder);
    }

    @Override
    public Schema payLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities, PayOrderEntity payOrder) throws BaseException {
        Schema shouldPaySchemaForEntry = this.mergeSchemas(this.loanOrderStrategyCouponSchemas(loanOrder, couponEntities), SchemaTypeEnum.ENTRY);
        return this.entrySchema(shouldPaySchemaForEntry, payOrder);
    }

    /**
     * 计算入账用schema
     * 传进来对应类型的schema就可以给出正确的入账schema
     * @param shouldPaySchema
     * @param payAmount
     * @return
     */
    private Schema entrySchema(Schema shouldPaySchema, PayOrderEntity payOrder) {
        Schema entrySchema = new Schema(SchemaTypeEnum.ENTRY);
        BigDecimal paid = payOrder.getEquivalentAmount();
        /**
         * 入账schema逻辑
         *
         * 只要入账逻辑是单个订单相关的， 不会几个订单杂糅到一起， 改这个方法中的代码就可以实现.
         * 如果订单杂糅在一起, 比如用户的逾期费全部一起入账, 则需要另写实现, 或者说需要重构
         *
         * 同一订单内决定入账顺序的元素有:
         * 1. 元素类型/元素入账类型
         * 2. 期数
         * 3. 还款日, 也就是到期还是没到期
         *
         * 根据以上参数, 重新整理schema来把需要入账的元素排序, 就可以入账了
         *
         * TODO: 扩展到多订单的时候也一样
         */
        for (Map.Entry<Integer, Instalment> instalmentEntry : shouldPaySchema.entrySet()) {
            if (BigDecimal.ZERO.compareTo(paid) >= 0) {
                break;
            }
            Integer instalmentKey = instalmentEntry.getKey();
            Instalment instalment = instalmentEntry.getValue();
            Instalment entryInstalment = entrySchema.get(instalmentKey);
            if (entryInstalment == null) {
                entryInstalment = new Instalment();
                entrySchema.put(instalmentKey, entryInstalment);
            }

            List<LoanOrderElementEnum> defaultEntrySequence = EntrySequenceEnum.getDefaultEntrySequence();
            for (LoanOrderElementEnum elementEnum : defaultEntrySequence) {
                if (BigDecimal.ZERO.compareTo(paid) >= 0) {
                    break;
                }
                Unit unit = instalment.get(elementEnum);
                if (unit != null && unit.size() > 0) {
                    Unit entryUnit = entryInstalment.get(elementEnum);
                    if (entryUnit == null) {
                        entryUnit = new Unit();
                        entryInstalment.put(elementEnum, entryUnit);
                    }

                    LoanOrderElementTypeEnum elementType = elementEnum.getLoanOrderElementType();
                    // 这种类型要做特殊处理, 把之后的期数都一起入账
                    // TODO: 可能有更好的处理方式
                    if (LoanOrderElementTypeEnum.PRIVILEGE.equals(elementType)) {
                        Integer totalInstalment = shouldPaySchema.size();
                        for (Integer i = 0; i < totalInstalment; i++) {
                            if (BigDecimal.ZERO.compareTo(paid) >= 0) {
                                break;
                            }
                            Instalment privilegeInstalment = shouldPaySchema.get(i);
                            Unit privilegeUnit = privilegeInstalment.get(elementEnum);
                            if (privilegeUnit != null && privilegeUnit.size() > 0) {
                                this.entryElement(privilegeUnit, entryUnit, paid, payOrder.getPayOrderId());
                            }
                        }
                    } else {
                        // 其他种类正常入账就行
                        this.entryElement(unit, entryUnit, paid, payOrder.getPayOrderId());
                    }
                }
            }
        }
        return entrySchema;
    }

    /**
     * 入账的元素入账逻辑
     */
    private void entryElement(Unit privilegeUnit, Unit entryUnit, BigDecimal paid, String payOrderId) {
        for (Element element : privilegeUnit) {
            // 如果source有值, 代表已经入账了
            Element cloneElement = element.clone();
            if (element.getSource() == null) {
                BigDecimal amount = element.getAmount();
                cloneElement.setSource(BillTypeEnum.PAY_ORDER);
                cloneElement.setSourceId(payOrderId);
                entryUnit.add(cloneElement);
                if (paid.compareTo(amount) > 0) {
                    paid = paid.subtract(amount);
                } else {
                    // 结束
                    cloneElement.setAmount(paid);
                    paid = BigDecimal.ZERO;
                }
            } else {
                entryUnit.add(cloneElement);
            }
        }
    }

    /**
     * 获得schema
     * @param loanOrder
     * @param couponEntities
     * @return
     * @throws BaseException
     */
    private List<Schema> loanOrderStrategyCouponSchemas(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) throws BaseException {
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
        return Arrays.asList(loanSchema, couponSchema, strategySchema);
    }

    /**
     * 获得schema
     * @param loanOrder
     * @return
     * @throws BaseException
     */
    private List<Schema> loanOrderCouponSchemas(LoanOrderEntity loanOrder) throws BaseException {
        // 订单schema
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        Schema currentSchema = instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
        Situation situation = SituationHolder.getSituation();
        List<StrategyEntity> strategyEntities = strategyService.listStrategies(situation);

        // 策略schema
        Schema strategySchema = strategyCalculator.getCurrentSchema(strategyEntities, loanOrder);
        return Arrays.asList(currentSchema, strategySchema);
    }

    /**
     * 合并schema
     * 思路:
     * 1. 把所有传进来的schema分成两类:  应还和已还(可以体现为减免等), 应还为正, 已还为负
     * 2. 用已还将应还抵消, 如果已还大于应还, 则直接抛异常(这里可优化), 如果小于等于, 正常扣掉已还就好
     *
     * 输出的schema是应还的schema
     *
     * @param schemas
     * @return
     * @throws SchemaException
     */
    private Schema mergeSchemas(List<Schema> schemas, SchemaTypeEnum schemaType) throws SchemaException {
        if (!SchemaTypeEnum.REPAY_RELEVANT.contains(schemaType)) {
            throw new SchemaException("mergeSchemas方法仅支持SchemaTypeEnum.ENTRY与SchemaTypeEnum.SHOULD_PAY, 当前schema类型" + schemaType.name());
        }
        // 应还schema
        Schema repaySchema = new Schema(schemaType);
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
                repayInstalment.setRepaymentDate(sourceInstalment.getRepaymentDate());
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
                        if (amount.add(sourceAmount).compareTo(BigDecimal.ZERO) > 0) {
                            // 不够还, 由于来源本来就是负的, 加上后放入应还schema
                            Element repayElement = element.clone();
                            repayElement.setAmount(amount.add(sourceAmount));
                            repayUnit.add(repayElement);
                            if (SchemaTypeEnum.ENTRY.equals(schemaType)) {
                                // 入账schema要加入已入账的明细
                                Element paidElement = element.clone();
                                element.setSource(sourceElement.getSource());
                                element.setSourceId(sourceElement.getSourceId());
                                // 入账后的值都是正的
                                // TODO: 有更好的方法吗?
                                element.setAmount(sourceAmount.abs());
                                repayUnit.add(paidElement);
                            }
                        } else if (amount.add(sourceAmount).compareTo(BigDecimal.ZERO) == 0) {
                            // 正好还完了
                            if (SchemaTypeEnum.ENTRY.equals(schemaType)) {
                                // 入账schema要加入已入账的明细
                                Element paidElement = element.clone();
                                element.setSource(sourceElement.getSource());
                                element.setSourceId(sourceElement.getSourceId());
                                // 入账后的值都是正的
                                // TODO: 有更好的方法吗?
                                element.setAmount(amount);
                                repayUnit.add(paidElement);
                            }
                        } else {
                            // 已还超过了应还, 抛异常
                            throw new SchemaException(element.getDestination().name() + "[" + element.getDestinationId() + "], 第" + instalmentKey + "期" + element.getElement().name() + "应还[" + amount.toPlainString() + "], 已还却有[" + sourceAmount +"], 不合法!");
                        }
                    } else {
                        // 已还的匹配完, 剩下的全是没还的
                        repayUnit.add(element.clone());
                    }
                }
            }
        }
        // 应还schema, 都是正的, 不用反转
        return repaySchema;
    }

    private Schema getWriteOffTypeSchema(List<Schema> schemas, SchemaTypeEnum writeOffSource) throws SchemaException {
        Schema writeOffSchema = new Schema(writeOffSource);
        for (Schema schema : schemas) {
            if (schema == null) {
                continue;
            }
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
                            if (instalment.getRepaymentDate() > 0) {
                                writeOffInstalment.setRepaymentDate(instalment.getRepaymentDate());
                            }
                            writeOffSchema.put(instalmentKey, writeOffInstalment);
                        }
                        if (SchemaTypeEnum.WRITE_OFF_SOURCE.equals(writeOffSource) && BigDecimal.ZERO.compareTo(amount) > 0) {
                            // 数字是负的在这儿
                            writeOffElements.add(element);
                            writeOffInstalment.put(elementKey, writeOffElements);
                            writeOffSchema.put(instalmentKey, writeOffInstalment);
                        }
                    }
                }
            }
        }
        return writeOffSchema;
    }
}
