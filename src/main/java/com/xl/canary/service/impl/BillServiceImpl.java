package com.xl.canary.service.impl;

import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.MixedSchema;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.bean.structure.Unit;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.engine.calculate.impl.InstalmentCalculatorImpl;
import com.xl.canary.engine.calculate.impl.StrategyCalculatorImpl;
import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.engine.calculate.siuation.SituationHolder;
import com.xl.canary.engine.calculate.siuation.SituationSource;
import com.xl.canary.engine.event.instalmet.InstalmentEntryEvent;
import com.xl.canary.engine.event.loan.LoanOrderEntryEvent;
import com.xl.canary.engine.event.pay.EntryLaunchEvent;
import com.xl.canary.engine.launcher.IEventLauncher;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.enums.loan.LoanOrderElementTypeEnum;
import com.xl.canary.enums.pay.EntrySequenceEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.exception.CouponException;
import com.xl.canary.exception.DateCalaulateException;
import com.xl.canary.exception.LoanEntryException;
import com.xl.canary.exception.SchemaException;
import com.xl.canary.service.BillService;
import com.xl.canary.service.CouponService;
import com.xl.canary.service.LoanInstalmentService;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.PayOrderDetailService;
import com.xl.canary.service.PayOrderService;
import com.xl.canary.service.StrategyService;
import com.xl.canary.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayOrderDetailService payOrderDetailService;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private IEventLauncher instalmentEventLauncher;

    @Autowired
    private IEventLauncher loanOrderEventLauncher;

    @Autowired
    private IEventLauncher payOrderEventLauncher;

    @Override
    public Schema payOffLoanOrder(LoanOrderEntity loanOrder) throws SchemaException, DateCalaulateException {
        // 订单schema
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        return instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    public Schema payOffLoanOrder(List<LoanInstalmentEntity> instalmentEntities) throws SchemaException, DateCalaulateException {
        return instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    public Schema payOffLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException {
        return this.mergeSchemas(this.loanOrderStrategySchemas(loanOrder), SchemaTypeEnum.SHOULD_PAY);
    }

    @Override
    public Schema payOffLoanOrderAndStrategy(List<LoanInstalmentEntity> instalmentEntities) throws BaseException {
        return this.mergeSchemas(this.loanOrderStrategySchemas(instalmentEntities), SchemaTypeEnum.SHOULD_PAY);
    }

    @Override
    public Schema payOffAll(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities) throws BaseException {
        return this.mergeSchemas(this.loanOrderStrategyCouponSchemas(loanOrder, couponEntities), SchemaTypeEnum.SHOULD_PAY);
    }

    @Override
    public Schema payOffAll(List<LoanInstalmentEntity> instalmentEntities, List<CouponEntity> couponEntities) throws BaseException {
        return this.mergeSchemas(this.loanOrderStrategyCouponSchemas(instalmentEntities, couponEntities), SchemaTypeEnum.SHOULD_PAY);
}

    @Override
    public Schema shouldPayLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException {
        Schema schema = this.mergeSchemas(this.loanOrderStrategySchemas(loanOrder), SchemaTypeEnum.SHOULD_PAY);
        return removeFutureInstalment(schema);
    }

    @Override
    public Schema shouldPayLoanOrderAndStrategy(List<LoanInstalmentEntity> instalmentEntities) throws BaseException {
        Schema schema = this.mergeSchemas(this.loanOrderStrategySchemas(instalmentEntities), SchemaTypeEnum.SHOULD_PAY);
        return removeFutureInstalment(schema);
    }

    @Override
    public Schema planLoanOrderAndStrategy(LoanOrderEntity loanOrder) throws BaseException {
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        return instalmentCalculator.getPlanSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    public Schema planLoanOrderAndStrategy(List<LoanInstalmentEntity> instalmentEntities) throws BaseException {
        return instalmentCalculator.getPlanSchema(System.currentTimeMillis(), instalmentEntities);
    }

    @Override
    @SituationSource
    public Schema payLoanOrder(LoanOrderEntity loanOrder, PayOrderEntity payOrder) throws BaseException {
        return this.entrySchema(this.loanOrderStrategySchemas(loanOrder), payOrder);
    }

    @Override
    @SituationSource
    public Schema payLoanOrder(List<LoanInstalmentEntity> instalmentEntities, PayOrderEntity payOrder) throws BaseException {
        return this.entrySchema(this.loanOrderStrategySchemas(instalmentEntities), payOrder);
    }

    @Override
    @SituationSource
    public Schema payLoanOrder(LoanOrderEntity loanOrder, List<CouponEntity> couponEntities, PayOrderEntity payOrder) throws BaseException {
        return this.entrySchema(this.loanOrderStrategyCouponSchemas(loanOrder, couponEntities), payOrder);
    }

    @Override
    @SituationSource
    public Schema payLoanOrder(List<LoanInstalmentEntity> instalmentEntities, List<CouponEntity> couponEntities, PayOrderEntity payOrder) throws BaseException {
        return this.entrySchema(this.loanOrderStrategyCouponSchemas(instalmentEntities, couponEntities), payOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchEntry(String payOrderId) throws Exception {
        PayOrderEntity payOrder = payOrderService.getByPayOrderId(payOrderId);
        if (!payOrder.getState().equals(StateEnum.DEDUCTED.name())) {
            return;
        }
        List<LoanOrderEntity> loanOrderEntities = loanOrderService.listByUserCode(payOrder.getUserCode(), StateEnum.lent);
        for (LoanOrderEntity loanOrderEntity : loanOrderEntities) {
            List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrderEntity.getOrderId());
            Schema realSchema = this.payOffLoanOrder(instalmentEntities);
            Schema entrySchema = this.payLoanOrder(instalmentEntities, payOrder);
            // 获得订单schema, 入账
            Schema orderEntrySchema = entrySchema.distinguishByDestination(BillTypeEnum.LOAN_ORDER);
            for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
                Integer instalment = instalmentEntity.getInstalment();
                Instalment orderEntryInstalment = orderEntrySchema.get(instalment);
                if (orderEntryInstalment != null) {
                    Instalment realInstalment = realSchema.get(instalment);
                    InstalmentEntryEvent instalmentEntryEvent = new InstalmentEntryEvent(instalmentEntity.getInstalmentId(), payOrder, orderEntryInstalment, realInstalment);
                    instalmentEventLauncher.launch(instalmentEntryEvent);
                }
            }
            // 如果完全还清, 则发送还清事件给订单
            if (orderEntrySchema.sum().compareTo(realSchema.sum()) == 0) {
                LoanOrderEntryEvent entryEvent = new LoanOrderEntryEvent(loanOrderEntity.getOrderId(), true);
                loanOrderEventLauncher.launch(entryEvent);
            }

            // 保存明细
            payOrderDetailService.saveScheme(payOrder, realSchema, entrySchema);
        }

        // 全部正常入账完成, 发送入账成功事件
        EntryLaunchEvent entryLaunchEvent = new EntryLaunchEvent(payOrderId);
        payOrderEventLauncher.launch(entryLaunchEvent);
    }

    /**
     * 计算入账用schema
     * 传进来对应类型的schema就可以给出正确的入账schema
     * @param schemas
     * @param payOrder
     * @return
     */
    private Schema entrySchema(List<Schema> schemas, PayOrderEntity payOrder) throws SchemaException, LoanEntryException {
        Long today = TimeUtils.truncateToDay(System.currentTimeMillis());
        // TODO
        /**
         * 优惠券/策略入账
         */
        Schema entrySchema = this.mergeSchemas(schemas, SchemaTypeEnum.ENTRY);

        /**
         * 剩余应还
         */
        Schema shouldPaySchema = this.mergeSchemas(schemas, SchemaTypeEnum.SHOULD_PAY);

        BigDecimal paid = payOrder.getEquivalentAmount().subtract(payOrder.getEntryAmount());
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
         * TODO: 如果是银行贷款的模式, 提前还款的话是需要本金利息按比例分配的， 这可以设置为一个可配置项
         * TODO: 根据策略/优惠券 类型， 将入账schema分为入到策略/优惠券/订单里
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

                    long repaymentDate = TimeUtils.truncateToDay(instalment.getRepaymentDate());
                    if (today < repaymentDate) {
                        // 提前还款, 利息, 本金比例分配
                        BigDecimal instalmentInterest = instalment.getElementAmount(LoanOrderElementEnum.INTEREST);
                        BigDecimal instalmentPrincipal = instalment.getElementAmount(LoanOrderElementEnum.PRINCIPAL);



                    } else {
                        LoanOrderElementTypeEnum elementType = elementEnum.getLoanOrderElementType();
                        // 这种类型要做特殊处理, 把之后的期数都一起入账
                        // TODO: 可能有更好的处理方式
                        if (LoanOrderElementTypeEnum.PRIVILEGE.equals(elementType)) {
                            Integer totalInstalment = shouldPaySchema.size();
                            for (Integer i = 1; i <= totalInstalment; i++) {
                                if (BigDecimal.ZERO.compareTo(paid) >= 0) {
                                    break;
                                }
                                Instalment privilegeInstalment = shouldPaySchema.get(i);
                                Unit privilegeUnit = privilegeInstalment.get(elementEnum);
                                if (privilegeUnit != null && privilegeUnit.size() > 0) {
                                    paid = this.entryElement(privilegeUnit, entryUnit, paid, payOrder.getPayOrderId());
                                }
                            }
                        } else {
                            // 其他种类正常入账就行
                            paid = this.entryElement(unit, entryUnit, paid, payOrder.getPayOrderId());
                        }
                    }
                }
            }
        }
        if (paid.compareTo(BigDecimal.ZERO) > 0) {
            throw new LoanEntryException("还款入账错误, 多还" + paid.toPlainString());
        }
        return entrySchema;
    }

    /**
     * 入账的元素入账逻辑
     */
    private BigDecimal entryElement(Unit privilegeUnit, Unit entryUnit, BigDecimal paid, String payOrderId) {
        for (Element element : privilegeUnit) {
            if (BigDecimal.ZERO.compareTo(paid) >= 0) {
                break;
            }
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
        return paid;
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

        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());

        // 策略schema
        Schema strategySchema = strategyCalculator.getCurrentSchema(strategyEntities, instalmentEntities);

        // 优惠券schema
        Boolean isPassed = couponService.checkCoupons(couponEntities);
        if (!isPassed) {
            throw new CouponException("优惠券集合中含有不可用优惠券");
        }
        Schema couponSchema = couponCalculator.getCurrentSchema(couponEntities, instalmentEntities);

        Schema loanSchema = instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
        return Arrays.asList(loanSchema, couponSchema, strategySchema);
    }

    /**
     * 获得schema
     * @param instalmentEntities
     * @param couponEntities
     * @return
     * @throws BaseException
     */
    private List<Schema> loanOrderStrategyCouponSchemas(List<LoanInstalmentEntity> instalmentEntities, List<CouponEntity> couponEntities) throws BaseException {
        Situation situation = SituationHolder.getSituation();
        List<StrategyEntity> strategyEntities = strategyService.listStrategies(situation);

        // 策略schema
        Schema strategySchema = strategyCalculator.getCurrentSchema(strategyEntities, instalmentEntities);

        // 优惠券schema
        Boolean isPassed = couponService.checkCoupons(couponEntities);
        if (!isPassed) {
            throw new CouponException("优惠券集合中含有不可用优惠券");
        }
        Schema couponSchema = couponCalculator.getCurrentSchema(couponEntities, instalmentEntities);

        Schema loanSchema = instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
        return Arrays.asList(loanSchema, couponSchema, strategySchema);
    }

    /**
     * 获得schema
     * @param loanOrder
     * @return
     * @throws BaseException
     */
    private List<Schema> loanOrderStrategySchemas(LoanOrderEntity loanOrder) throws BaseException {
        // 订单schema
        List<LoanInstalmentEntity> instalmentEntities = instalmentService.listInstalments(loanOrder.getOrderId());
        Schema currentSchema = instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
        Situation situation = SituationHolder.getSituation();
        List<StrategyEntity> strategyEntities = strategyService.listStrategies(situation);

        // 策略schema
        Schema strategySchema = strategyCalculator.getCurrentSchema(strategyEntities, instalmentEntities);
        return Arrays.asList(currentSchema, strategySchema);
    }

    /**
     * 获得schema
     * @param instalmentEntities
     * @return
     * @throws BaseException
     */
    private List<Schema> loanOrderStrategySchemas(List<LoanInstalmentEntity> instalmentEntities) throws BaseException {
        Schema currentSchema = instalmentCalculator.getCurrentSchema(System.currentTimeMillis(), instalmentEntities);
        Situation situation = SituationHolder.getSituation();
        List<StrategyEntity> strategyEntities = strategyService.listStrategies(situation);

        // 策略schema
        Schema strategySchema = strategyCalculator.getCurrentSchema(strategyEntities, instalmentEntities);
        return Arrays.asList(currentSchema, strategySchema);
    }

    /**
     * 合并schema
     * 思路:
     * 1. 把所有传进来的schema分成两类:  应还和已还(可以体现为减免等), 应还为正, 已还为负
     * 2. 用已还将应还抵消, 如果已还大于应还, 则直接抛异常(这里可优化), 如果小于等于, 正常扣掉已还就好
     *
     * SchemaTypeEnum.SHOULD_PAY : 输出的schema是应还的schema
     * SchemaTypeEnum.ENTRY : 输出的schema是全额还款时 入账优惠/策略后的已入账schema
     *
     * @param schemas
     * @return
     * @throws SchemaException
     */
    private Schema mergeSchemas(List<Schema> schemas, SchemaTypeEnum schemaType) throws SchemaException {
        if (!SchemaTypeEnum.REPAY_RELEVANT.contains(schemaType)) {
            throw new SchemaException("mergeSchemas方法仅支持SchemaTypeEnum.ENTRY与SchemaTypeEnum.SHOULD_PAY, 当前schema类型" + schemaType.name());
        }
        long now = System.currentTimeMillis();

        // 应还schema
        Schema repaySchema = new MixedSchema(schemaType);
        Schema sourceSchema = getWriteOffTypeSchema(schemas, SchemaTypeEnum.WRITE_OFF_SOURCE);
        Schema destinationSchema = getWriteOffTypeSchema(schemas, SchemaTypeEnum.WRITE_OFF_DESTINATION);

        // 分配应还schema
        for (Map.Entry<Integer, Instalment> instalmentEntry : destinationSchema.entrySet()) {
            Integer instalmentKey = instalmentEntry.getKey();
            Instalment instalment = instalmentEntry.getValue();
            if (instalment.getRepaymentDate() > now) {
                // 未出账单, 需要另外入账
            }
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

                //Element orderBaseElement = unit.get(0);
                //unit.addAll(sourceUnit);
                //BigDecimal couponPercentInverse = unit.getCouponPercentInverse();
                //BigDecimal couponNumber = unit.getCouponNumber();

                // unit中有部分还了
                Element sourceElement = null;
                Element element = null;
                Iterator<Element> unitIterator = unit.iterator();
                Iterator<Element> sourceIterator = sourceUnit.iterator();
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal sourceAmount = BigDecimal.ZERO;
                while (true) {
                    if (sourceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        // 上一轮已还完, 继续取下一个已还
                        if (sourceIterator.hasNext()) {
                            sourceElement = sourceIterator.next();
                            sourceAmount = sourceElement.getAmount();
                        } else {
                            break;
                        }
                    }
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        // 上一轮已还完, 继续取下一个已还
                        if (unitIterator.hasNext()) {
                            element = unitIterator.next();
                            amount = element.getAmount();
                        } else {
                            break;
                        }
                    }

                    if (amount.add(sourceAmount).compareTo(BigDecimal.ZERO) > 0) {
                        // 加入已入账的明细
                        this.addPaidElement(element, sourceElement, sourceAmount.abs(), repayUnit);
                        // 由于来源本来就是负的, 所以相加
                        amount = amount.add(sourceAmount);
                        sourceAmount = BigDecimal.ZERO;
                        element.setAmount(amount);
                        sourceElement.setAmount(sourceAmount);
                    } else if (amount.add(sourceAmount).compareTo(BigDecimal.ZERO) == 0) {
                        // 正好还完了, 加入已入账的明细
                        this.addPaidElement(element, sourceElement, amount, repayUnit);
                        amount = BigDecimal.ZERO;
                        sourceAmount = BigDecimal.ZERO;
                        sourceElement.setAmount(amount);
                        element.setAmount(sourceAmount);
                    } else {
                        // 已还的要多
                        this.addPaidElement(element, sourceElement, amount, repayUnit);
                        sourceAmount = amount.add(sourceAmount);
                        amount = BigDecimal.ZERO;
                        element.setAmount(amount);
                        sourceElement.setAmount(sourceAmount);
                    }
                }
                // 已还的要多, 抛异常
                if (sourceAmount.compareTo(BigDecimal.ZERO) != 0 || sourceIterator.hasNext()) {
                    // 已还超过了应还, 抛异常, TODO: 根据需求, 可优化为按照最大可还入账
                    throw new SchemaException(element.getDestination().name() + "[" + element.getDestinationId() + "], 第" + instalmentKey + "期" + element.getElement().name() + "还玩应还后, 还有[" + sourceAmount +"]以上, 不合法!");
                }
            }
        }
        // 应还schema, 都是正数
        return repaySchema;
    }

    // 对比后, 添加element
    public void addPaidElement (Element element, Element sourceElement, BigDecimal amount, Unit repayUnit) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            Element paidElement = element.clone();
            element.setSource(sourceElement.getSource());
            element.setSourceId(sourceElement.getSourceId());
            // 入账后的值都是正的
            element.setAmount(amount);
            repayUnit.add(paidElement);
        }
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

    /**
     * 去除还没到期的期数
     * @param schema
     */
    private Schema removeFutureInstalment (Schema schema) {
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
}
