package com.xl.canary.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 订单要素枚举
 * 注意: 如果这里增加要素, 则 EntrySequenceEnum 中要决定他的入账顺序
 * created by XUAN on 2018/08/19
 */
public enum LoanOrderElementEnum {

    /**
     * 本金
     */
    PRINCIPAL(LoanOrderElementTypeEnum.GENERAL_INSTALMENT),
    /**
     * 利息
     */
    INTEREST(LoanOrderElementTypeEnum.GENERAL_INSTALMENT),
    /**
     * 罚息
     */
    PENALTY(LoanOrderElementTypeEnum.PRIVILEGE),
    /**
     * 服务费
     */
    SERVICE_FEE(LoanOrderElementTypeEnum.GENERAL_INSTALMENT),
    /**
     * 地址生成费
     */
    GENERATE_ADDRESS_FEE(LoanOrderElementTypeEnum.NULL),
    /**
     * 地址绑定费
     */
    BIND_ADDRESS_FEE(LoanOrderElementTypeEnum.NULL),

    ;

    LoanOrderElementTypeEnum loanOrderElementType;

    /**
     * 跟随分期的费用
     */
    public static final List<String> FEE_FOLLOW_INSTALMENT = Arrays.asList(SERVICE_FEE.name());

    /**
     * 绑定分期费用
     */
    public static final List<String> FEE = Arrays.asList(GENERATE_ADDRESS_FEE.name(), BIND_ADDRESS_FEE.name());
    /**
     * 可以用于最后入账的要素
     */
    public static final List<LoanOrderElementEnum> THE_LASTS = Arrays.asList(PRINCIPAL, SERVICE_FEE, GENERATE_ADDRESS_FEE, BIND_ADDRESS_FEE);

    LoanOrderElementEnum() {
    }

    LoanOrderElementEnum(LoanOrderElementTypeEnum loanOrderElementType) {
        this.loanOrderElementType = loanOrderElementType;
    }
}
