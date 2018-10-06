package com.xl.canary.bean.res;

import com.xl.canary.bean.structure.Schema;

/**
 * created by XUAN on 2018/10/06
 */
public class ShouldPayRes {

    /**
     * 应还
     */
    private Schema shouldPayAmount;

    /**
     * 还清
     */
    private Schema payoffAmount;

    /**
     * 正常到期应还
     */
    private Schema planAmount;

    public Schema getShouldPayAmount() {
        return shouldPayAmount;
    }

    public void setShouldPayAmount(Schema shouldPayAmount) {
        this.shouldPayAmount = shouldPayAmount;
    }

    public Schema getPayoffAmount() {
        return payoffAmount;
    }

    public void setPayoffAmount(Schema payoffAmount) {
        this.payoffAmount = payoffAmount;
    }

    public Schema getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(Schema planAmount) {
        this.planAmount = planAmount;
    }
}
