package com.xl.canary.engine.calculate;

import com.xl.canary.bean.structure.Instalment;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.ISchemaEntity;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.exception.SchemaException;

import java.util.List;

/**
 * 基础计算器
 * Created by xzhang on 2018/9/18.
 */
public interface SchemaCalculator {

    /**
     * 获取订单的原始账单, 下单时的账单, 一分未还的账单
     * @param schemaEntities   订单号
     * @return
     */
    Schema getOriginalSchema(List<? extends ISchemaEntity> schemaEntities) throws SchemaException;
}
