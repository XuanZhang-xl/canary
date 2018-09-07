package com.xl.canary.handle.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handle.condition.CouponConditionHandler;
import com.xl.canary.handle.condition.DefaultCouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 规定可用此优惠券的还款币种handler
 * Created by xzhang on 2018/9/7.
 */
@Component("payCurrencyConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.PAY_CURRENCY)
public class PayCurrencyConditionHandler extends DefaultCouponConditionHandler {
}
