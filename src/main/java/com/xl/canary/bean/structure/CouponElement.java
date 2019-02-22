package com.xl.canary.bean.structure;

import com.xl.canary.enums.WeightEnum;

import java.io.Serializable;

/**
 * 优惠券专用
 * Created by xzhang on 2018/9/6.
 */
public class CouponElement extends Element implements Cloneable, Serializable {

    /**
     * 比重, 如果是比例, 则扣款的时候也要按比例去扣款
     */
    private WeightEnum weight;

    public WeightEnum getWeight() {
        return weight;
    }

    public void setWeight(WeightEnum weight) {
        this.weight = weight;
    }
}
