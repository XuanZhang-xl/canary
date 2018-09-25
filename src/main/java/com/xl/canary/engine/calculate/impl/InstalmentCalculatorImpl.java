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
    private PayOrderDetailService payOrderDetailService;

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
        String instalmentUnit = loanOrder.getInstalmentUnit();
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
            principalElement.setSource(BillTypeEnum.LOAN_ORDER);
            principalElement.setSourceId(instalmentEntity.getOrderId());
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
            interestElement.setSource(BillTypeEnum.LOAN_ORDER);
            interestElement.setSourceId(instalmentEntity.getOrderId());
            interestUnit.add(interestElement);
            instalment.put(LoanOrderElementEnum.INTEREST, interestUnit);

            // 加入罚息
            if (now > shouldPayTime) {
                Unit penaltyUnit = new Unit();
                Element penaltyElement = new Element();
                passDays = TimeUtils.passDays(now, shouldPayTime, instalmentEntity.getTimeZone());
                penaltyElement.setAmount(originalPrincipal.multiply(loanOrder.getPenaltyRate()).multiply(new BigDecimal(passDays)));
                penaltyElement.setElement(LoanOrderElementEnum.PENALTY);
                penaltyElement.setSource(BillTypeEnum.LOAN_ORDER);
                penaltyElement.setSourceId(instalmentEntity.getOrderId());
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
                feeElement.setSource(BillTypeEnum.LOAN_ORDER);
                feeElement.setSourceId(instalmentEntity.getOrderId());
                feeUnit.add(feeElement);
                instalment.put(loanOrderElementEnum, feeUnit);
            }

            // TODO: instalment 中的原始还款日要不要加?

            schema.put(instalmentEntity.getInstalment(), instalment);
        }
        return schema;
    }

    /**
     * 1号借, 30号还, 周期30天, 如果今天1号, 则返回1/30, 今天2号,返回2/30
     * @param instalmentUnit
     * @param today
     * @param shouldPayTime
     * @param timeZone
     * @return
     */
    private BigDecimal passPercent(String instalmentUnit, Long today, Long shouldPayTime, Integer timeZone) {
        if (today >= shouldPayTime) {
            return BigDecimal.ONE;
        }
        Integer days = TimeUnitEnum.valueOf(instalmentUnit).getDays();
        int instalmentMilliSeconds = days * EssentialConstance.DAY_MILLISECOND;
        if (today + instalmentMilliSeconds < shouldPayTime) {
            return BigDecimal.ZERO;
        } else {
            Calendar todayCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
            todayCalendar.setTimeInMillis(today);
            todayCalendar = TimeUtils.truncateToDay(todayCalendar);
            Calendar shouldPayCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneEnum.getZoneId(timeZone)));
            shouldPayCalendar.setTimeInMillis(shouldPayTime);
            shouldPayCalendar = TimeUtils.truncateToDay(shouldPayCalendar);

            // todayCalendar 与 shouldPayTime都应为当地时间的0点
            long diff = todayCalendar.getTimeInMillis() - (shouldPayCalendar.getTimeInMillis() - instalmentMilliSeconds);
            return new BigDecimal(diff + EssentialConstance.DAY_MILLISECOND).divide(new BigDecimal(instalmentMilliSeconds), LoanLimitation.RESULT_SCALE, LoanLimitation.RESULT_UP);
        }
    }

    @Override
    public Schema getCurrentSchema(Long date, List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        // 检查传入的实体是否符合要求
        List<LoanInstalmentEntity> instalmentEntities = checkSchemaEntity(schemaEntities);
        String orderId = instalmentEntities.get(0).getOrderId();
        Schema paySchema = payOrderDetailService.recoverSchemaByOrderId(orderId, BillTypeEnum.PAY_ORDER);

        Schema schema = new Schema(SchemaTypeEnum.LOAN_CURRENT);
        for (LoanInstalmentEntity instalmentEntity : instalmentEntities) {
            if (!StateEnum.LENT.name().equals(instalmentEntity.getState())) {
                // 只有lent状态的分期才会有schema
                continue;
            }
            Instalment instalment = new Instalment();
            // 加入原始本金
            BigDecimal principal = instalmentEntity.getPrincipal();
            Unit principalUnit = new Unit();
            Element principalElement = new Element();
            principalElement.setAmount(principal);
            principalElement.setElement(LoanOrderElementEnum.PRINCIPAL);
            principalElement.setSource(BillTypeEnum.LOAN_ORDER);
            principalElement.setSourceId(instalmentEntity.getOrderId());
            principalUnit.add(principalElement);
            instalment.put(LoanOrderElementEnum.PRINCIPAL, principalUnit);
            // 加入利息
            BigDecimal originalInterest = instalmentEntity.getOriginalInterest();
            Unit interestUnit = new Unit();
            Element interestElement = new Element();
            interestElement.setAmount(originalInterest);
            interestElement.setElement(LoanOrderElementEnum.INTEREST);
            interestElement.setSource(BillTypeEnum.LOAN_ORDER);
            interestElement.setSourceId(instalmentEntity.getOrderId());
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
                feeElement.setSource(BillTypeEnum.LOAN_ORDER);
                feeElement.setSourceId(instalmentEntity.getOrderId());
                feeUnit.add(feeElement);
                instalment.put(loanOrderElementEnum, feeUnit);
            }

            // TODO: instalment 中的原始还款日要不要加?

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
            principalElement.setSource(BillTypeEnum.LOAN_ORDER);
            principalElement.setSourceId(instalmentEntity.getOrderId());
            principalUnit.add(principalElement);
            instalment.put(LoanOrderElementEnum.PRINCIPAL, principalUnit);
            // 加入利息
            BigDecimal originalInterest = instalmentEntity.getOriginalInterest();
            Unit interestUnit = new Unit();
            Element interestElement = new Element();
            interestElement.setAmount(originalInterest);
            interestElement.setElement(LoanOrderElementEnum.INTEREST);
            interestElement.setSource(BillTypeEnum.LOAN_ORDER);
            interestElement.setSourceId(instalmentEntity.getOrderId());
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
                feeElement.setSource(BillTypeEnum.LOAN_ORDER);
                feeElement.setSourceId(instalmentEntity.getOrderId());
                feeUnit.add(feeElement);
                instalment.put(loanOrderElementEnum, feeUnit);
            }

            // TODO: instalment 中的原始还款日要不要加?

            schema.put(instalmentEntity.getInstalment(), instalment);
        }
        return schema;
    }

    @Override
    public Map<Integer, Long> repaymentDates(List<? extends ISchemaEntity> schemaEntities) {
        return null;
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
