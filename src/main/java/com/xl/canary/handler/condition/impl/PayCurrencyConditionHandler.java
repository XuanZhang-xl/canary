package com.xl.canary.handler.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handler.condition.ArithmeticDescribeCouponConditionHandler;
import com.xl.canary.handler.condition.CouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 规定可用此优惠券的还款币种handler
 * Created by xzhang on 2018/9/7.
 */
@Component("payCurrencyConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.PAY_CURRENCY)
public class PayCurrencyConditionHandler extends ArithmeticDescribeCouponConditionHandler {
}
