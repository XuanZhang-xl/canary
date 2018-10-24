package com.xl.canary.engine.calculate.impl;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.bean.structure.Unit;
import com.xl.canary.engine.calculate.LoanSchemaCalculator;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.*;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.exception.DateCalaulateException;
import com.xl.canary.exception.LoanEntryException;
import com.xl.canary.exception.SchemaException;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.utils.EssentialConstance;
import com.xl.canary.utils.LoanLimitation;
import com.xl.canary.utils.LoanOrderInstalmentUtils;
import com.xl.canary.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 分期schema计算器
 * created by XUAN on 2018/09/08
 */
@Component("instalmentCalculator")
public class InstalmentCalculatorImpl implements LoanSchemaCalculator {

    Logger logger = LoggerFactory.getLogger(InstalmentCalculatorImpl.class);

    @Autowired
    private LoanOrderService loanOrderService;

    /**
     * 似乎并没有什么卵用
     * @param schemaEntities   订单号
     * @return
     * @throws SchemaException
     */
    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) throws SchemaException, DateCalaulateException {
        // 检查传入的实体是否符合要求
        List<LoanInstalmentEntity> instalmentEntities = checkSchemaEntity(schemaEntities);
        String orderId = instalmentEntities.get(0).getOrderId();
        LoanOrderEntity loanOrder = loanOrderService.getByOrderId(orderId);
        // 分期利率
        BigDecimal instalmentRate = loanOrder.getInstalmentRate();
        Long now = System.currentTimeMillis();

        Schema schema = new Schema(SchemaTypeEnum.LOAN_ORIGINAL);
        for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
            // 原始账单, 不检查状态
            Instalment instalment = new Instalment();
            // 加入原始本金
            BigDecimal originalPrincipal = instalmentEntity.getOriginalPrincipal();
            Unit principalUnit = new Unit();
            Element principalElement = new Element();
            principalElement.setAmount(originalPrincipal);
            principalElement.setElement(LoanOrderElementEnum.PRINCIPAL);
            principalElement.setInstalment(instalmentEntity.getInstalment());
            principalElement.setDestination(BillTypeEnum.LOAN_ORDER);
            principalElement.setDestinationId(instalmentEntity.getOrderId());
            principalUnit.add(principalElement);
            instalment.put(LoanOrderElementEnum.PRINCIPAL, principalUnit);

            // 加入利息
            Long shouldPayTime = instalmentEntity.getShouldPayTime();
            TimeUnitEnum timeUnit = TimeUnitEnum.valueOf(loanOrder.getInstalmentUnit());
            Integer passDays = TimeUtils.passDays(shouldPayTime - EssentialConstance.DAY_MILLISECOND * timeUnit.getDays(), now, instalmentEntity.getTimeZone());
            BigDecimal dailyInterest = LoanOrderInstalmentUtils.dailyInterest(originalPrincipal, timeUnit, instalmentRate);
            Unit interestUnit = new Unit();
            Element interestElement = new Element();
            interestElement.setAmount(dailyInterest.multiply(new BigDecimal(passDays)));
            interestElement.setElement(LoanOrderElementEnum.INTEREST);
            interestElement.setInstalment(instalmentEntity.getInstalment());
            interestElement.setDestination(BillTypeEnum.LOAN_ORDER);
            interestElement.setDestinationId(instalmentEntity.getOrderId());
            interestUnit.add(interestElement);
            instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);

            // 加入罚息
            if (now > shouldPayTime) {
                Unit penaltyUnit = new Unit();
                Element penaltyElement = new Element();
                passDays = TimeUtils.passDays(now, shouldPayTime, instalmentEntity.getTimeZone());
                BigDecimal dailyPenalty = LoanOrderInstalmentUtils.dailyPenalty(originalPrincipal, timeUnit, loanOrder.getPenaltyRate());
                penaltyElement.setAmount(originalPrincipal.multiply(dailyPenalty.multiply(new BigDecimal(passDays))));
                penaltyElement.setElement(LoanOrderElementEnum.PENALTY);
                penaltyElement.setInstalment(instalmentEntity.getInstalment());
                penaltyElement.setDestination(BillTypeEnum.LOAN_ORDER);
                penaltyElement.setDestinationId(instalmentEntity.getOrderId());
                interestUnit.add(penaltyElement);
                instalment.put(LoanOrderElementEnum.PENALTY, penaltyUnit);
            }

            // 加入各种服务费
            String originalFee = instalmentEntity.getOriginalFee();
            JSONObject feeJson = JSONObject.parseObject(originalFee);
            for (Map.Entry<String, Object> entry : feeJson.entrySet()) {
                String feeName = entry.getKey();
                LoanOrderElementEnum loanOrderElementEnum = LoanOrderElementEnum.valueOf(feeName);
                BigDecimal fee = feeJson.getBigDecimal(feeName);
                Unit feeUnit = new Unit();
                Element feeElement = new Element();
                feeElement.setAmount(fee);
                feeElement.setElement(loanOrderElementEnum);
                feeElement.setInstalment(instalmentEntity.getInstalment());
                feeElement.setDestination(BillTypeEnum.LOAN_ORDER);
                feeElement.setDestinationId(instalmentEntity.getOrderId());
                feeUnit.add(feeElement);
                instalment.put(loanOrderElementEnum, feeUnit);
            }

            // TODO: instalment 中的原始还款日要不要加?

            schema.put(instalmentEntity.getInstalment(), instalment);
        }
        return schema;
    }

    /**
     * 只有这里需要比较复杂的入账逻辑, 单独列一个方法
     * @param schemaEntity
     * @param payOrder
     * @param entryInstalment
     * @param realInstalment
     * @return
     */
    public ISchemaEntity entryInstalment(ISchemaEntity schemaEntity, PayOrderEntity payOrder, Instalment entryInstalment, Instalment realInstalment) throws BaseException {
        // 检查传入的实体是否符合要求
        LoanInstalmentEntity instalmentEntity = checkSchemaEntity(Arrays.asList(schemaEntity)).get(0);

        BigDecimal entrySum = entryInstalment.sum();
        BigDecimal realSum = realInstalment.sum();

        Long repaymentDate = TimeUtils.truncateToDay(instalmentEntity.getShouldPayTime());
        Long today = TimeUtils.truncateToDay(System.currentTimeMillis());
        if (repaymentDate > today) {
            /**
             *  TODO: 如果是银行贷款的模式, 提前还款的话是需要重新分期的, 这可以设置为一个可配置项
             */
            partRepayEntry(instalmentEntity, entryInstalment);
        } else {
            // 按期, 逾期还款
            if (realSum.compareTo(entrySum) == 0) {
                instalmentEntity.setPaidPrincipal(BigDecimal.ZERO);
                instalmentEntity.setPaidInterest(BigDecimal.ZERO);
                instalmentEntity.setPaidPenalty(BigDecimal.ZERO);
                instalmentEntity.setLastPaidPrincipalDate(today);
                String paidFee = instalmentEntity.getPaidFee();
                JSONObject feeJson = JSONObject.parseObject(paidFee);
                for (Map.Entry<String, Object> entry : feeJson.entrySet()) {
                    feeJson.put(entry.getKey(), "0");
                }
                instalmentEntity.setPaidFee(feeJson.toJSONString());
                instalmentEntity.setInstalmentState(StateEnum.PAYOFF.name());
            } else if (realSum.compareTo(entrySum) > 0) {
                partRepayEntry(instalmentEntity, entryInstalment);
            } else {
                logger.error("订单[{}]第[{}]期, 应还[{}], 实际还款[{}], 无法入账!", instalmentEntity.getOrderId(), instalmentEntity.getInstalment(), realSum, entrySum);
                throw new LoanEntryException("订单" + instalmentEntity.getOrderId() + "第" + instalmentEntity.getInstalment() + "期, 应还" + realSum + ", 实际还款" + entrySum + ", 无法入账!");
            }
        }
        return instalmentEntity;
    }

    /**
     * 部分还款入账逻辑
     * @param instalmentEntity
     * @param entryInstalment
     */
    private void partRepayEntry(LoanInstalmentEntity instalmentEntity, Instalment entryInstalment) {
        // 修正已付本金
        BigDecimal currentPaidPrincipal = entryInstalment.getElementAmount(LoanOrderElementEnum.PRINCIPAL);
        instalmentEntity.setPaidPrincipal(instalmentEntity.getPaidPrincipal().add(currentPaidPrincipal));
        // 修正已付利息
        BigDecimal currentPaidInterest = entryInstalment.getElementAmount(LoanOrderElementEnum.INTEREST);
        instalmentEntity.setPaidInterest(instalmentEntity.getPaidInterest().add(currentPaidInterest));
        // 修正已付罚息
        BigDecimal currentPaidPenalty = entryInstalment.getElementAmount(LoanOrderElementEnum.PENALTY);
        instalmentEntity.setPaidPenalty(instalmentEntity.getPaidPenalty().add(currentPaidPenalty));
        // 修正已付手续费
        String paidFee = instalmentEntity.getPaidFee();
        JSONObject feeJson = JSONObject.parseObject(paidFee);
        for (Map.Entry<String, Object> entry : feeJson.entrySet()) {
            String key = entry.getKey();
            BigDecimal currentPaidFee = entryInstalment.getElementAmount(LoanOrderElementEnum.valueOf(key));
            feeJson.put(entry.getKey(), feeJson.getBigDecimal(key).add(currentPaidFee).toPlainString());
        }
        instalmentEntity.setPaidFee(feeJson.toJSONString());
    }

    /**
     * 暂时先依赖每日结算
     * @param date   用户时间, 一般都为当前时间
     * @param schemaEntities   借款订单
     * @return
     * @throws SchemaException
     */
    @Override
    public Schema getCurrentSchema(Long date, List<? extends ISchemaEntity> schemaEntities) throws SchemaException, DateCalaulateException {
        // 检查传入的实体是否符合要求
        List<LoanInstalmentEntity> instalmentEntities = checkSchemaEntity(schemaEntities);

        /**
         * TODO: 这样取特别别扭, 可不可以外面传进来?
         */
        String orderId = instalmentEntities.get(0).getOrderId();
        LoanOrderEntity loanOrder = loanOrderService.getByOrderId(orderId);

        Schema schema = new Schema(SchemaTypeEnum.LOAN_CURRENT);
        for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
            if (!StateEnum.LENT.name().equals(instalmentEntity.getState())) {
                // 只有lent状态的分期才会有schema
                continue;
            }
            Instalment instalment = new Instalment();
            // 加入本金
            Unit principalUnit = new Unit();
            Element principalElement = new Element();
            principalElement.setAmount(instalmentEntity.getOriginalPrincipal().subtract(instalmentEntity.getPaidPrincipal()));
            principalElement.setElement(LoanOrderElementEnum.PRINCIPAL);
            principalElement.setInstalment(instalmentEntity.getInstalment());
            principalElement.setDestination(BillTypeEnum.LOAN_ORDER);
            principalElement.setDestinationId(instalmentEntity.getOrderId());
            principalUnit.add(principalElement);
            instalment.put(LoanOrderElementEnum.PRINCIPAL, principalUnit);

            // 加入各种服务费
            String originalFee = instalmentEntity.getOriginalFee();
            String paidFee = instalmentEntity.getPaidFee();
            JSONObject originalFeeJson = JSONObject.parseObject(originalFee);
            JSONObject paidFeeJson = JSONObject.parseObject(paidFee);
            for (Map.Entry<String, Object> entry : originalFeeJson.entrySet()) {
                String feeName = entry.getKey();
                LoanOrderElementEnum loanOrderElementEnum = LoanOrderElementEnum.valueOf(feeName);
                BigDecimal fee = originalFeeJson.getBigDecimal(feeName);
                Unit feeUnit = new Unit();
                Element feeElement = new Element();
                feeElement.setAmount(fee.subtract(paidFeeJson.getBigDecimal(feeName)));
                feeElement.setElement(loanOrderElementEnum);
                feeElement.setInstalment(instalmentEntity.getInstalment());
                feeElement.setDestination(BillTypeEnum.LOAN_ORDER);
                feeElement.setDestinationId(instalmentEntity.getOrderId());
                feeUnit.add(feeElement);
                instalment.put(loanOrderElementEnum, feeUnit);
            }

            Long today = TimeUtils.truncateToDay(date);
            Long repaymentDate = TimeUtils.truncateToDay(instalmentEntity.getShouldPayTime());
            // 加入利息, 到期和未到期的利息计算方式不同
            if (today > repaymentDate) {
                // 逾期
                // 加入罚息
                Unit penaltyUnit = new Unit();
                Element penaltyElement = new Element();

                Long lastPaidPrincipalDate = instalmentEntity.getLastPaidPrincipalDate();
                Long penaltyBeginDate = lastPaidPrincipalDate > repaymentDate ? lastPaidPrincipalDate : repaymentDate;
                long overDueDays = TimeUtils.passDays(penaltyBeginDate, today, instalmentEntity.getTimeZone());
                BigDecimal dailyPenalty = LoanOrderInstalmentUtils.dailyPenalty(principalElement.getAmount(), TimeUnitEnum.valueOf(loanOrder.getInstalmentUnit()), loanOrder.getPenaltyRate());
                /**
                 * 罚息每天一记
                 */
                penaltyElement.setAmount(dailyPenalty
                        .multiply(new BigDecimal(overDueDays))
                        .subtract(instalmentEntity.getPaidPenalty())
                        .setScale(LoanLimitation.RESULT_SCALE, LoanLimitation.RESULT_UP));
                penaltyElement.setElement(LoanOrderElementEnum.PENALTY);
                penaltyElement.setInstalment(instalmentEntity.getInstalment());
                penaltyElement.setDestination(BillTypeEnum.LOAN_ORDER);
                penaltyElement.setDestinationId(instalmentEntity.getOrderId());
                penaltyUnit.add(penaltyElement);
                instalment.put(LoanOrderElementEnum.PENALTY, penaltyUnit);

                // 加入利息
                Unit interestUnit = new Unit();
                Element interestElement = new Element();
                interestElement.setAmount(instalmentEntity.getOriginalInterest().subtract(instalmentEntity.getPaidInterest()));
                interestElement.setElement(LoanOrderElementEnum.INTEREST);
                interestElement.setInstalment(instalmentEntity.getInstalment());
                interestElement.setDestination(BillTypeEnum.LOAN_ORDER);
                interestElement.setDestinationId(instalmentEntity.getOrderId());
                interestUnit.add(interestElement);
                instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);
            } else if (today.equals(repaymentDate)) {
                // 到期
                // 加入利息
                Unit interestUnit = new Unit();
                Element interestElement = new Element();
                interestElement.setAmount(instalmentEntity.getOriginalInterest().subtract(instalmentEntity.getPaidInterest()));
                interestElement.setElement(LoanOrderElementEnum.INTEREST);
                interestElement.setInstalment(instalmentEntity.getInstalment());
                interestElement.setDestination(BillTypeEnum.LOAN_ORDER);
                interestElement.setDestinationId(instalmentEntity.getOrderId());
                interestUnit.add(interestElement);
                instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);
            } else {
                // 未到期
                // 加入利息
                Unit interestUnit = new Unit();
                Element interestElement = new Element();

                long passedDays = TimeUtils.passDays(loanOrder.getLentTime(), today, instalmentEntity.getTimeZone());
                BigDecimal dailyInterest = LoanOrderInstalmentUtils.dailyInterest(principalElement.getAmount(), TimeUnitEnum.valueOf(loanOrder.getInstalmentUnit()), loanOrder.getInstalmentRate());

                /**
                 * 利息每天一记
                 */
                interestElement.setAmount(dailyInterest
                        .multiply(new BigDecimal(passedDays))
                        .subtract(instalmentEntity.getPaidInterest())
                        .setScale(LoanLimitation.RESULT_SCALE, LoanLimitation.RESULT_UP));
                interestElement.setElement(LoanOrderElementEnum.INTEREST);
                interestElement.setInstalment(instalmentEntity.getInstalment());
                interestElement.setDestination(BillTypeEnum.LOAN_ORDER);
                interestElement.setDestinationId(instalmentEntity.getOrderId());
                interestUnit.add(interestElement);
                instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);
            }

            // 加入还款日
            instalment.setRepaymentDate(repaymentDate);
            schema.put(instalmentEntity.getInstalment(), instalment);
        }
        return schema;
    }

    @Override
    public Schema getPlanSchema(Long date, List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        // 检查传入的实体是否符合要求
        List<LoanInstalmentEntity> instalmentEntities = checkSchemaEntity(schemaEntities);

        Schema schema = new Schema(SchemaTypeEnum.LOAN_ORIGINAL);
        for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
            // 原始账单, 不检查状态
            Instalment instalment = new Instalment();
            // 加入原始本金
            BigDecimal originalPrincipal = instalmentEntity.getOriginalPrincipal();
            Unit principalUnit = new Unit();
            Element principalElement = new Element();
            principalElement.setAmount(originalPrincipal);
            principalElement.setElement(LoanOrderElementEnum.PRINCIPAL);
            principalElement.setInstalment(instalmentEntity.getInstalment());
            principalElement.setDestination(BillTypeEnum.LOAN_ORDER);
            principalElement.setDestinationId(instalmentEntity.getOrderId());
            principalUnit.add(principalElement);
            instalment.put(LoanOrderElementEnum.PRINCIPAL, principalUnit);
            // 加入利息
            BigDecimal originalInterest = instalmentEntity.getOriginalInterest();
            Unit interestUnit = new Unit();
            Element interestElement = new Element();
            interestElement.setAmount(originalInterest);
            interestElement.setElement(LoanOrderElementEnum.INTEREST);
            interestElement.setInstalment(instalmentEntity.getInstalment());
            interestElement.setDestination(BillTypeEnum.LOAN_ORDER);
            interestElement.setDestinationId(instalmentEntity.getOrderId());
            interestUnit.add(interestElement);
            instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);
            // 加入各种服务费
            String originalFee = instalmentEntity.getOriginalFee();
            JSONObject feeJson = JSONObject.parseObject(originalFee);
            for (Map.Entry<String, Object> entry : feeJson.entrySet()) {
                String feeName = entry.getKey();
                LoanOrderElementEnum loanOrderElementEnum = LoanOrderElementEnum.valueOf(feeName);
                BigDecimal fee = feeJson.getBigDecimal(feeName);
                Unit feeUnit = new Unit();
                Element feeElement = new Element();
                feeElement.setAmount(fee);
                feeElement.setElement(loanOrderElementEnum);
                feeElement.setInstalment(instalmentEntity.getInstalment());
                feeElement.setDestination(BillTypeEnum.LOAN_ORDER);
                feeElement.setDestinationId(instalmentEntity.getOrderId());
                feeUnit.add(feeElement);
                instalment.put(loanOrderElementEnum, feeUnit);
            }

            // TODO: instalment 中的原始还款日要不要加?

            schema.put(instalmentEntity.getInstalment(), instalment);
        }
        return schema;
    }

    @Override
    public Map<Integer, Long> repaymentDates(List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        // 检查传入的实体是否符合要求
        List<LoanInstalmentEntity> instalmentEntities = checkSchemaEntity(schemaEntities);
        Map<Integer, Long> repaymentDates = new HashMap<Integer, Long>();
        for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
            repaymentDates.put(instalmentEntity.getInstalment(), TimeUtils.truncateToDay(instalmentEntity.getShouldPayTime()));
        }
        return repaymentDates;
    }

    private List<LoanInstalmentEntity> checkSchemaEntity (List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        if (schemaEntities == null || schemaEntities.size() == 0) {
            throw new SchemaException("分期计算器传入实体为空!");
        }
        List<LoanInstalmentEntity> instalmentEntities = new ArrayList<LoanInstalmentEntity>();
        // 检查传入参数是否正确
        String orderId = null;
        for (ISchemaEntity schemaEntity : schemaEntities) {
            if (!(schemaEntity instanceof LoanInstalmentEntity)) {
                throw new SchemaException("分期计算器传入实体并非分期! 实体分期类型为" + schemaEntity.getSchemaType().name());
            }
            LoanInstalmentEntity instalmentEntity = (LoanInstalmentEntity) schemaEntity;
            if (orderId == null) {
                orderId = instalmentEntity.getOrderId();
            }
            if (!orderId.equals(instalmentEntity.getOrderId())) {
                throw new SchemaException("分期计算器传入实体并非来自同一个订单, id" + orderId + "  以及id" + instalmentEntity.getOrderId());
            }
            instalmentEntities.add(instalmentEntity);
        }
        return instalmentEntities;
    }
}
