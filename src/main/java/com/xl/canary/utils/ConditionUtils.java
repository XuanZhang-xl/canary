package com.xl.canary.utils;

import com.xl.canary.bean.dto.Situation;
import com.xl.canary.enums.coupon.CouponConditionEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xzhang on 2018/9/13.
 */
public class ConditionUtils {

    /**
     * 限制条件的所有则正匹配表达式
     */
    private static final Map<CouponConditionEnum, Pattern> PATTERNS = new HashMap<CouponConditionEnum, Pattern>();

    static {
        for (CouponConditionEnum couponCondition : CouponConditionEnum.values()) {
            PATTERNS.put(couponCondition, Pattern.compile("\\$\\{" + couponCondition.name() + "}"));
        }
    }

    public static String replacePlaceholder (String targetStr, Situation situation) {
        for (Map.Entry<CouponConditionEnum, String> entry : situation.entrySet()) {
            Matcher matcher = PATTERNS.get(entry.getKey()).matcher(targetStr);
            if (matcher.find()) {
                targetStr = matcher.replaceAll(situation.get(entry.getKey()));
            }
        }
        return targetStr;
    }


    public static void main(String[] args){
        String testStr = "begin   ${LOAN_CURRENCY}  +++ ${LOAN_AMOUNT}   end";
        Situation situation = new Situation().collect(CouponConditionEnum.LOAN_CURRENCY, "BTC").collect(CouponConditionEnum.LOAN_AMOUNT, 100);
        for (Map.Entry<CouponConditionEnum, Pattern> entry : PATTERNS.entrySet()) {
            Matcher matcher = entry.getValue().matcher(testStr);
            if (matcher.find()) {
                testStr = matcher.replaceAll(situation.get(entry.getKey()));
            }
        }
        System.out.println(testStr);
    }
}
