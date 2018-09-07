package com.xl.canary.handler.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handler.condition.ArithmeticDescribeCouponConditionHandler;
import com.xl.canary.handler.condition.CouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 期数限定handler
 * Created by xzhang on 2018/9/7.
 */
@Component("instalmentConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.INSTALMENT)
public class InstalmentConditionHandler extends ArithmeticDescribeCouponConditionHandler {
}
