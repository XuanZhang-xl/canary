package com.xl.canary.enums;

import java.math.BigDecimal;

/**
 * created by XUAN on 2018/09/02
 */
public enum WeightEnum {

    /**
     * 百分比
     */
    PERCENT(BigDecimal.ZERO),

    /**
     * 固定值
     */
    NUMBER(BigDecimal.ZERO),

    ;

    private BigDecimal param;

    WeightEnum(BigDecimal param) {
        this.param = param;
    }

    public BigDecimal getParam() {
        return param;
    }

    public void setParam(BigDecimal param) {
        this.param = param;
    }
}
