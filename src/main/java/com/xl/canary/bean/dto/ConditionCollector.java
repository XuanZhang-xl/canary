package com.xl.canary.bean.dto;

import com.xl.canary.entity.AbstractOrderEntity;
import com.xl.canary.enums.SubjectEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 条件收集器
 * 每次输入都创建一个新实例
 * Created by xzhang on 2018/9/12.
 */
@Component("conditionCollector")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConditionCollector {

    /**
     * 用于存储order, 条件来源
     */
    private Map<SubjectEnum, AbstractOrderEntity> entities = new HashMap<SubjectEnum, AbstractOrderEntity>();

    /**
     * 参数
     */
    private Situation situation = new Situation();

    public ConditionCollector addOrder (AbstractOrderEntity order) {
        entities.put(order.getOrderSubjectType(), order);
        return this;
    }

    /**
     * 从订单中获取数据
     * @return
     */
    public Situation getSituation() {
        for (SubjectEnum subjectEnum : SubjectEnum.values()) {
            AbstractOrderEntity order = entities.get(subjectEnum);
            if (order == null) {
                continue;
            }
            if (SubjectEnum.PAY_ORDER.equals(order.getOrderSubjectType())) {
                situation.put(CouponConditionEnum.PAY_AMOUNT, order.getEquivalentAmount());
                situation.put(CouponConditionEnum.PAY_CURRENCY, order.getEquivalent());
            }
            if (SubjectEnum.LOAN_ORDER.equals(order.getOrderSubjectType())) {
                situation.put(CouponConditionEnum.LOAN_AMOUNT, order.getEquivalentAmount());
                situation.put(CouponConditionEnum.LOAN_CURRENCY, order.getEquivalent());
            }
        }
        return situation;
    }

    /**
     * 直接添加数据
     * @param condition
     * @param comparable
     * @return
     */
    public Situation addSituation(CouponConditionEnum condition, Comparable comparable) {
        situation.put(condition, comparable);
        return situation;
    }
}
