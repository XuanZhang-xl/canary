package com.xl.canary.engine.calculate.impl;

import com.xl.canary.bean.structure.Element;
import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.bean.structure.Unit;
import com.xl.canary.engine.calculate.CouponCalculator;
import com.xl.canary.engine.calculate.LoanSchemaCalculator;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.BillTypeEnum;
import com.xl.canary.enums.SchemaTypeEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.WeightEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.enums.loan.LoanOrderElementEnum;
import com.xl.canary.exception.SchemaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 优惠券schema计算器实现类
 * Created by xzhang on 2018/9/18.
 */
@Component("couponCalculator")
public class CouponCalculatorImpl implements CouponCalculator {

    @Autowired
    private LoanSchemaCalculator loanSchemaCalculator;

    @Override
    public Schema getCurrentSchema(List<? extends ISchemaEntity> schemaEntities, LoanOrderEntity loanOrder) throws SchemaException {
        List<CouponEntity> couponEntities = checkSchemaEntity(schemaEntities, loanOrder.getOrderId());

        Schema orderSchema = loanSchemaCalculator.getCurrentSchema(System.currentTimeMillis(), Arrays.asList(loanOrder));

        // 正的为惩罚, 负的为优惠
        BigDecimal orderAmount;
        Schema schema = new Schema(SchemaTypeEnum.COUPON);
        for (CouponEntity couponEntity : couponEntities) {
            String couponState = couponEntity.getCouponState();
            if (!StateEnum.coupon.contains(StateEnum.valueOf(couponState))) {
                throw new SchemaException("优惠券状态" + couponState + "不可做schema");
            }
            Integer instalment = couponEntity.getInstalment();
            String couponElement = couponEntity.getElement();
            LoanOrderElementEnum elementKey = LoanOrderElementEnum.valueOf(couponElement);

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
            CouponTypeEnum couponType = CouponTypeEnum.valueOf(couponEntity.getCouponType());
            WeightEnum weight = couponType.getWeight();
            BigDecimal amount = this.getApplyAmount(weight, couponEntity.getDefaultAmount(), orderAmount);
            element.setAmount(amount.subtract(couponEntity.getEntryAmount()));
            element.setElement(elementKey);
            element.setSource(BillTypeEnum.COUPON);
            element.setInstalment(instalment);
            element.setSourceId(couponEntity.getCouponBatchId());
            unit.add(element);
        }
        return schema.reverse();
    }

    @Override
    public Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) throws SchemaException {
        throw new SchemaException("优惠券计算器不支持OriginalSchema");
    }

    private List<CouponEntity> checkSchemaEntity (List<? extends ISchemaEntity> schemaEntities, String orderId) throws SchemaException {
        if (schemaEntities == null || schemaEntities.size() == 0) {
            throw new SchemaException("优惠券计算器传入实体为空!");
        }
        List<CouponEntity> couponEntities = new ArrayList<CouponEntity>();
        // 检查传入参数是否正确
        String couponBatchId = null;
        String boundOrderId = null;
        for (ISchemaEntity schemaEntity : schemaEntities) {
            // 1. 检查类型
            if (!(schemaEntity instanceof CouponEntity)) {
                throw new SchemaException("优惠券计算器传入实体并非优惠券! 实体类型为" + schemaEntity.getSchemaType().name());
            }
            // 2. 检查couponBatchId是否一致
            CouponEntity couponEntity = (CouponEntity) schemaEntity;
            if (couponBatchId == null) {
                couponBatchId = couponEntity.getCouponBatchId();
            }
            if (!couponBatchId.equals(couponEntity.getCouponBatchId())) {
                throw new SchemaException("优惠券计算器传入实体并非来自同一个订单, id" + couponBatchId + "  以及id" + couponEntity.getCouponBatchId());
            }
            // 3. 检查 boundOrderId 是否一致
            if (boundOrderId == null || boundOrderId.equals(couponEntity.getBoundOrderId())) {
                boundOrderId = couponEntity.getBoundOrderId();
            } else {
                throw new SchemaException("优惠券同一个couponBatchId绑定了不同的订单   " + couponEntity.getBoundOrderId() + "  " + boundOrderId);
            }
            // 4. 检查通过, 添加
            couponEntities.add(couponEntity);
        }

        if (boundOrderId != null && !boundOrderId.equals(orderId)) {
            throw  new SchemaException("优惠券" + couponBatchId + "绑定了订单" + boundOrderId + " 而传入订单为 " + orderId);
        }
        return couponEntities;
    }

    @Override
    public BigDecimal getApplyAmount(WeightEnum weight, BigDecimal defaultAmount, BigDecimal orderAmount) {
        if (WeightEnum.NUMBER.equals(weight)) {
            return defaultAmount;
        } else {
            return orderAmount.multiply(defaultAmount);
        }
    }

}
