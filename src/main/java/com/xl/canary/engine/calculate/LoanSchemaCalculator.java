package com.xl.canary.engine.calculate;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.exception.SchemaException;

import java.util.List;
import java.util.Map;

/**
 * schema计算器
 * created by XUAN on 2018/09/08
 */
public interface LoanSchemaCalculator extends SchemaCalculator {

    /**
     * 获取订单的当前账单, 当前的立马还清的账单
     * @param schemaEntities   借款订单
     * @param date   用户时间, 一般都为当前时间
     * @return
     */
    Schema getCurrentSchema(Long date, List<? extends ISchemaEntity> schemaEntities) throws SchemaException;

    /**
     * 获取订单的计划账单, 当前时间到订单结束如果不提前还款, 在还款日还清的账单
     * @param schemaEntities   借款订单
     * @param date   用户时间, 一般都为当前时间
     * @return
     */
    Schema getPlanSchema(Long date, List<? extends ISchemaEntity> schemaEntities);

    /**
     * 还款日
     * @param schemaEntities   借款订单
     * @return
     */
    Map<Integer, Long> repaymentDates(List<? extends ISchemaEntity> schemaEntities);

}
