package com.xl.canary.handler.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handler.condition.ArithmeticDescribeCouponConditionHandler;
import com.xl.canary.handler.condition.CouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 规定可用此优惠券的借款币种handler
 * Created by xzhang on 2018/9/7.
 */
@Component("loanCurrencyConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.LOAN_CURRENCY)
public class LoanCurrencyConditionHandler extends ArithmeticDescribeCouponConditionHandler {
}
