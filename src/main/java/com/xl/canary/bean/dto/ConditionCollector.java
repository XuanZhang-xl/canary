package com.xl.canary.bean.dto;

import com.xl.canary.bean.structure.Schema;
import com.xl.canary.entity.IOrderEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.coupon.CouponConditionEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件收集器
 * 每次输入都创建一个新实例
 * Created by xzhang on 2018/9/12.
 */
public class ConditionCollector {

    /**
     * 用于存储order, 条件来源
     */
    private List<Object> entities = new ArrayList<Object>();

    public ConditionCollector addSource (IOrderEntity order) {
        entities.add(order);
        return this;
    }

    public ConditionCollector addSource (Schema schema) {
        entities.add(schema);
        return this;
    }

    /**
     * 从订单中获取数据
     * @return
     */
    public Situation getSituation() {
        Situation situation = new Situation();
        for (Object entity : entities) {
            if (entity instanceof LoanOrderEntity) {
                LoanOrderEntity order = (LoanOrderEntity) entity;
                situation.collect(CouponConditionEnum.LOAN_AMOUNT, order.getEquivalentAmount());
                situation.collect(CouponConditionEnum.LOAN_CURRENCY, order.getEquivalent());
            }
            if (entity instanceof PayOrderEntity) {
                PayOrderEntity order = (PayOrderEntity) entity;
                situation.collect(CouponConditionEnum.PAY_AMOUNT, order.getEquivalentAmount());
                situation.collect(CouponConditionEnum.PAY_CURRENCY, order.getEquivalent());
            }
            if (entity instanceof Schema) {
                Schema schema = (Schema) entity;
                // TODO
            }
        }
        return situation;
    }

}
