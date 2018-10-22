package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.bean.structure.Unit;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.engine.calculate.LoanSchemaCalculator;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.WeightEnum;
import com.xl.canary.enums.coupon.StrategyTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.SchemaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 策略计算器实现类
 * Created by xzhang on 2018/9/18.
 */
@Component("strategyCalculator")
public class StrategyCalculatorImpl implements CouponCalculator {

    @Autowired
    private LoanSchemaCalculator loanSchemaCalculator;

    @Override
    public Schema getCurrentSchema(List<? extends ISchemaEntity> schemaEntities, LoanOrderEntity loanOrder) throws SchemaException {
        if (schemaEntities == null || schemaEntities.size() == 0) {
            return null;
        }
        List<StrategyEntity> strategyEntities = this.checkSchemaEntity(schemaEntities);

        Schema orderSchema = loanSchemaCalculator.getCurrentSchema(System.currentTimeMillis(), Arrays.asList(loanOrder));

        // 正的为惩罚, 负的为优惠
        BigDecimal orderAmount;
        Schema schema = new Schema(SchemaTypeEnum.STRATEGY);

        for (StrategyEntity strategyEntity : strategyEntities) {
            Integer instalment = strategyEntity.getInstalment();
            String strategyElement = strategyEntity.getElement();
            LoanOrderElementEnum elementKey = LoanOrderElementEnum.valueOf(strategyElement);

            Instalment schemaInstalment = schema.get(instalment);
            if (schemaInstalment == null) {
                schemaInstalment = new Instalment();
                schema.put(instalment, schemaInstalment);
            }
            Unit unit = schemaInstalment.get(elementKey);
            if (unit == null) {
                unit = new Unit();
                schemaInstalment.put(elementKey, unit);
            }

            // 获取orderScehma的amount, 如果是没有, 则默认是0
            Instalment orderInstalment = orderSchema.get(instalment);
            if (orderInstalment == null) {
                orderAmount = BigDecimal.ZERO;
            } else {
                Element orderElement = orderInstalment.get(elementKey).get(0);
                if (orderElement == null) {
                    orderAmount = BigDecimal.ZERO;
                } else {
                    orderAmount = orderElement.getAmount();
                }
            }

            Element element = new Element();
            StrategyTypeEnum strategyType = StrategyTypeEnum.valueOf(strategyEntity.getStrategyType());
            WeightEnum weight = strategyType.getWeight();
            BigDecimal amount = this.getApplyAmount(weight, strategyEntity.getDefaultAmount(), orderAmount);
            element.setAmount(amount);
            element.setElement(elementKey);
            element.setInstalment(instalment);
            element.setSource(BillTypeEnum.STRATEGY);
            element.setSourceId(strategyEntity.getStrategyId());
            unit.add(element);
        }
        return schema.reverse();
    }

    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        throw new SchemaException("策略计算器不支持OriginalSchema");
    }

    @Override
    public BigDecimal getApplyAmount(WeightEnum weight, BigDecimal defaultAmount, BigDecimal orderAmount) {
        if (WeightEnum.NUMBER.equals(weight)) {
            return defaultAmount;
        } else {
            return orderAmount.multiply(defaultAmount);
        }
    }

    private List<StrategyEntity> checkSchemaEntity (List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        List<StrategyEntity> strategyEntities = new ArrayList<StrategyEntity>();
        // 检查传入参数是否正确
        for (ISchemaEntity schemaEntity : schemaEntities) {
            // 1. 检查类型
            if (!(schemaEntity instanceof StrategyEntity)) {
                throw new SchemaException("优惠券计算器传入实体并非优惠券! 实体类型为" + schemaEntity.getSchemaType().name());
            }
            StrategyEntity strategyEntity = (StrategyEntity) schemaEntity;

            strategyEntities.add(strategyEntity);
        }
        return strategyEntities;
    }
}
