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
        switch (repaymentDateType) {
            case FIX_MONTH_INTERVAL: {
                Calendar calendar = Calendar.getInstance();
                // 当天也算一天, 要减一
                calendar.setTimeInMillis(System.currentTimeMillis() - EssentialConstance.DAY_MILLISECOND);
                // 存数据库的都不要截取, 取出来后在代码中截取
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
