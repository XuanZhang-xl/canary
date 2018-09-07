package com.xl.canary.handle.condition.impl;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.handle.aoperator.ArithmeticOperatorFactory;
import com.xl.canary.handle.aoperator.IArithmeticOperator;
import com.xl.canary.handle.condition.CouponConditionHandler;
import com.xl.canary.handle.condition.DefaultCouponConditionHandler;
import com.xl.canary.handle.condition.ICouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 可使用元素handler
 * Created by xzhang on 2018/9/7.
 */
@Component("elementConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.ELEMENT)
public class ElementConditionHandler extends DefaultCouponConditionHandler {
}
