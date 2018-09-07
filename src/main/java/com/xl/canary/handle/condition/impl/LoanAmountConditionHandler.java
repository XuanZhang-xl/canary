package com.xl.canary.handle.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handle.condition.CouponConditionHandler;
import com.xl.canary.handle.condition.DefaultCouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 规定可用此优惠券的借款金额handler
 * Created by xzhang on 2018/9/7.
 */
@Component("loanAmountConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.LOAN_AMOUNT)
public class LoanAmountConditionHandler extends DefaultCouponConditionHandler {
}
