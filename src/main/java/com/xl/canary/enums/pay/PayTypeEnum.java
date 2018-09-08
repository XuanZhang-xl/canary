package com.xl.canary.enums.pay;

import java.util.Arrays;
import java.util.List;

/**
 * created by XUAN on 2018/08/23
 */
public enum PayTypeEnum {

    /**
     * 之前的到期清偿
     */
    LIQUIDATE_BY_OVERDUE("逾期清偿"),
    LIQUIDATE_BY_WAVE("波动清偿"),
    LIQUIDATE_BY_WAVE_AND_BROKE("波动清偿-破产"),


    REPAY_IN_ADVANCE("提前还款"),
    REPAY_AS_PLAN("按期还款"),
    REPAY_OVERDUE("逾期还款"),
    SYSTEM_REPAY_AS_PLAN("系统按期还款"),

    DEDUCT("减免"),
    ;

    private String explanation;

    public String getExplanation() {
        return explanation;
    }

    PayTypeEnum(String explanation){
        this.explanation = explanation;
    }

    /**
     * 清偿
     */
    public static final List<PayTypeEnum> LIQUIDATE = Arrays.asList(LIQUIDATE_BY_OVERDUE, LIQUIDATE_BY_WAVE, LIQUIDATE_BY_WAVE_AND_BROKE);

    /**
     * 波动清偿
     */
    public static final List<PayTypeEnum> WAVE_LIQUIDATE = Arrays.asList(LIQUIDATE_BY_WAVE, LIQUIDATE_BY_WAVE_AND_BROKE);

    /**
     * 一般
     */
    public static final List<PayTypeEnum> NORMAL = Arrays.asList(REPAY_AS_PLAN, REPAY_IN_ADVANCE, REPAY_OVERDUE);


}
