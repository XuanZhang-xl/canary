package com.xl.canary.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by gqwu on 2018/3/23.
 * 分期还款方式
 */
public enum LoanOrderTypeEnum {

    FIXED_INSTALLMENT("等额本息"),
    FIXED_PRINCIPAL("等额本金"),
    ;

    private String explanation;

    LoanOrderTypeEnum(String explanation){
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }


    public static LoanOrderTypeEnum getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return FIXED_INSTALLMENT;
        }
        LoanOrderTypeEnum[] settleTypes = LoanOrderTypeEnum.values();
        for (LoanOrderTypeEnum settleType : settleTypes) {
            if (settleType.name().equals(name)) {
                return settleType;
            }
        }
        return FIXED_INSTALLMENT;
    }



}
