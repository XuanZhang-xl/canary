package com.xl.canary.handler.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handler.condition.ArithmeticDescribeCouponConditionHandler;
import com.xl.canary.handler.condition.CouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 使用场合handler
 * Created by xzhang on 2018/9/7.
 */
@Component("occasionConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.OCCASION)
public class OccasionConditionHandler extends ArithmeticDescribeCouponConditionHandler {
}
