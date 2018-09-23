package com.xl.canary.engine.calculate.siuation;

import com.xl.canary.enums.coupon.CouponConditionEnum;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 当前条件收集器
 * Created by xzhang on 2018/9/12.
 */
public class Situation extends HashMap<CouponConditionEnum, String> {

    public Situation collect(CouponConditionEnum key, String value) {
        this.put(key, value);
        return this;
    }

    public Situation collect(CouponConditionEnum key, BigDecimal value) {
        this.collect(key, value.toPlainString());
        return this;
    }

    public Situation collect(CouponConditionEnum key, Long value) {
        this.collect(key, value.toString());
        return this;
    }


    public Situation collect(CouponConditionEnum key, Integer value) {
        this.collect(key, value.toString());
        return this;
    }
}
