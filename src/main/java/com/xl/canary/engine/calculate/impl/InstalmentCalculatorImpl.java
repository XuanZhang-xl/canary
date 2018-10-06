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
import com.xl.canary.enums.*;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.SchemaException;
import com.xl.canary.service.LoanOrderService;
import com.xl.canary.service.PayOrderDetailService;
import com.xl.canary.utils.EssentialConstance;
import com.xl.canary.utils.LoanLimitation;
import com.xl.canary.utils.LoanOrderInstalmentUtils;
import com.xl.canary.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 分期schema计算器
 * created by XUAN on 2018/09/08
 */
@Component("instalmentCalculatorImpl")
public class InstalmentCalculatorImpl implements LoanSchemaCalculator {

    @Autowired
    private LoanOrderService loanOrderService;

    /**
     * 似乎并没有什么卵用
     * @param schemaEntities   订单号
     * @return
     * @throws SchemaException
     */
    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
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
     * 暂时先依赖每日结算
     * @param date   用户时间, 一般都为当前时间
     * @param schemaEntities   借款订单
     * @return
     * @throws SchemaException
     */
    @Override
    public Schema getCurrentSchema(Long date, List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        // 检查传入的实体是否符合要求
        List<LoanInstalmentEntity> instalmentEntities = checkSchemaEntity(schemaEntities);

        Schema schema = new Schema(SchemaTypeEnum.LOAN_CURRENT);
        for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
            if (!StateEnum.LENT.name().equals(instalmentEntity.getState())) {
                // 只有lent状态的分期才会有schema
                continue;
            }
            Instalment instalment = new Instalment();
            // 加入本金
            BigDecimal principal = instalmentEntity.getPrincipal();
            Unit principalUnit = new Unit();
            Element principalElement = new Element();
            principalElement.setAmount(principal);
            principalElement.setElement(LoanOrderElementEnum.PRINCIPAL);
            principalElement.setInstalment(instalmentEntity.getInstalment());
            principalElement.setDestination(BillTypeEnum.LOAN_ORDER);
            principalElement.setDestinationId(instalmentEntity.getOrderId());
            principalUnit.add(principalElement);
            instalment.put(LoanOrderElementEnum.PRINCIPAL, principalUnit);
            // 加入利息
            BigDecimal interest = instalmentEntity.getInterest();
            Unit interestUnit = new Unit();
            Element interestElement = new Element();
            interestElement.setAmount(interest);
            interestElement.setElement(LoanOrderElementEnum.INTEREST);
            interestElement.setInstalment(instalmentEntity.getInstalment());
            interestElement.setDestination(BillTypeEnum.LOAN_ORDER);
            interestElement.setDestinationId(instalmentEntity.getOrderId());
            interestUnit.add(interestElement);
            instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);

            // 加入罚息
            BigDecimal penalty = instalmentEntity.getPenalty();
            if (penalty != null && BigDecimal.ZERO.compareTo(penalty) < 0) {
                Unit penaltyUnit = new Unit();
                Element penaltyElement = new Element();
                penaltyElement.setAmount(penalty);
                penaltyElement.setElement(LoanOrderElementEnum.PENALTY);
                penaltyElement.setInstalment(instalmentEntity.getInstalment());
                penaltyElement.setDestination(BillTypeEnum.LOAN_ORDER);
                penaltyElement.setDestinationId(instalmentEntity.getOrderId());
                interestUnit.add(penaltyElement);
                instalment.put(LoanOrderElementEnum.PENALTY, penaltyUnit);
            }

            // 加入各种服务费
            String totalFee = instalmentEntity.getFee();
            JSONObject feeJson = JSONObject.parseObject(totalFee);
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

            // 加入还款日
            instalment.setRepaymentDate(TimeUtils.truncateToDay(instalmentEntity.getShouldPayTime()));

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
