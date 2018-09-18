package com.xl.canary.enums.pay;

/**
 * 入账模式
 * Created by xzhang on 2018/9/18.
 */
public enum EntryModeEnum {

    /**
     * 按照账单还款日期入账, 哪个账单先到期先入哪个账单
     */
    BY_REPAYMENT_DATE(),

    /**
     * 按照订单借款日期的先后顺序入账, 谁先借, 谁先完全入账至还清
     */
    BY_LOAN_ORDER(),

    ;

    /**
     * 根据还款类型决定使用哪种入账方式
     * @param payTypeEnum    还款类型
     * @return
     */
    public static EntryModeEnum getByPayType(PayTypeEnum payTypeEnum) {
        return BY_REPAYMENT_DATE;
    }
}
