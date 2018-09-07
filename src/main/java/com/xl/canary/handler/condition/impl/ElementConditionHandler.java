package com.xl.canary.handler.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handler.condition.ArithmeticDescribeCouponConditionHandler;
import com.xl.canary.handler.condition.CouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 可使用元素handler
 * Created by xzhang on 2018/9/7.
 */
@Component("elementConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.ELEMENT)
public class ElementConditionHandler extends ArithmeticDescribeCouponConditionHandler {
}
