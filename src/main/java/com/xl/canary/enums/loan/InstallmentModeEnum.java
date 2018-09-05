package com.xl.canary.enums.loan;

import com.xl.canary.entity.LoanOrderEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * 分期方式
 * created by XUAN on 2018/08/19
 */
public enum InstallmentModeEnum {

    /**
     * 等额本息
     */
    FIXED_INSTALLMENT(),
    /**
     * 等额本金
     */
    FIXED_PRINCIPAL(),
    ;


    public void initLoanOrder(LoanOrderEntity loanOrder, InstallmentModeEnum installmentMode){

    }

    public static InstallmentModeEnum getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return FIXED_INSTALLMENT;
        }
        InstallmentModeEnum[] settleTypes = InstallmentModeEnum.values();
        for (InstallmentModeEnum settleType : settleTypes) {
            if (settleType.name().equals(name)) {
                return settleType;
            }
        }
        return FIXED_INSTALLMENT;
    }


}
