package com.xl.canary.enums.loan;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xzhang on 2018/8/19.
 * 分期还款方式
 */
public enum LoanOrderTypeEnum {

    /**
     * 一般类型, 测试用
     */
    GENERAL_TYPE("一般类型", RepaymentDateTypeEnum.FIX_MONTH_INTERVAL, InstallmentModeEnum.FIXED_INSTALLMENT),
    ;

    /**
     * 订单类型描述
     */
    private String desc;

    /**
     * 订单类型的还款日
     */
    private RepaymentDateTypeEnum repaymentDateType;

    /**
     * 订单类型的分期方式
     */
    private InstallmentModeEnum installmentMode;

    public String getDesc() {
        return desc;
    }

    public RepaymentDateTypeEnum getRepaymentDateType() {
        return repaymentDateType;
    }

    public InstallmentModeEnum getInstallmentMode() {
        return installmentMode;
    }

    LoanOrderTypeEnum(String desc, RepaymentDateTypeEnum repaymentDateType, InstallmentModeEnum installmentMode) {
        this.desc = desc;
        this.repaymentDateType = repaymentDateType;
        this.installmentMode = installmentMode;
    }

    public static LoanOrderTypeEnum getByName(String name) {
        if (StringUtils.isBlank(name)) {
            return GENERAL_TYPE;
        }
        LoanOrderTypeEnum[] loanOrderTypes = LoanOrderTypeEnum.values();
        for (LoanOrderTypeEnum loanOrderType : loanOrderTypes) {
            if (loanOrderType.name().equals(name)) {
                return loanOrderType;
            }
        }
        return GENERAL_TYPE;
    }
}
