package com.xl.canary.handle.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handle.condition.CouponConditionHandler;
import com.xl.canary.handle.condition.DefaultCouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 规定可用此优惠券的还款金额handler
 * Created by xzhang on 2018/9/7.
 */
@Component("payAmountConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.PAY_AMOUNT)
public class PayAmountConditionHandler extends DefaultCouponConditionHandler {
}
