package com.xl.canary.bean.dto;

import com.xl.canary.enums.coupon.CouponConditionEnum;

import java.util.HashMap;

/**
 * 客观条件
 * Created by xzhang on 2018/9/12.
 */
public class Situation extends HashMap<CouponConditionEnum, Comparable> {

    public Situation collect(CouponConditionEnum key, Comparable value) {
        this.put(key, value);
        return this;
    }
}
