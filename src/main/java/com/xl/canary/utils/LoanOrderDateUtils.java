package com.xl.canary.utils;

import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.enums.loan.LoanOrderTypeEnum;
import com.xl.canary.enums.loan.RepaymentDateTypeEnum;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * created by XUAN on 2018/08/21
 */
public class LoanOrderDateUtils {

    public static Map<Integer, Long> listRepaymentDates (LoanOrderEntity loanOrder) {
        Map<Integer, Long> repayments = new HashMap<Integer, Long>();
        LoanOrderTypeEnum loanOrderType = LoanOrderTypeEnum.valueOf(loanOrder.getOrderType());
        RepaymentDateTypeEnum repaymentDateType = loanOrderType.getRepaymentDateType();
        Integer instalment = loanOrder.getInstalment();
        loanOrder.getInstalmentUnit();
        switch (repaymentDateType) {
            case FIX_MONTH_INTERVAL: {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                for (Integer i = 1; i <= instalment; i++) {
                    calendar.add(Calendar.DAY_OF_MONTH, EssentialConstance.MONTH_DAYS);
                    repayments.put(i, calendar.getTimeInMillis());
                }
                break;
            }
            // TODO: 补足其他类型
            default:{
            }
        }
        return repayments;
    }

}
