package com.xl.canary.engine.calculate;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.enums.WeightEnum;
import com.xl.canary.exception.DateCalaulateException;
import com.xl.canary.exception.SchemaException;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券计算器
 * Created by xzhang on 2018/9/18.
 */
public interface CouponCalculator extends SchemaCalculator {

    /**
     * 获取订单的当前账单, 当前的立马还清的账单
     * @param schemaEntities   优惠券, 策略
     * @param instalmentEntities, 优惠百分比时依赖于借款订单
     * @return   schema
     */
    Schema getCurrentSchema(List<? extends ISchemaEntity> schemaEntities, List<LoanInstalmentEntity> instalmentEntities) throws SchemaException, DateCalaulateException;

    /**
     * 根据比重获取实际的金额
     * @param weight
     * @param defaultAmount
     * @param orderAmount
     * @return
     */
    BigDecimal getApplyAmount(WeightEnum weight, BigDecimal defaultAmount, BigDecimal orderAmount);
}
