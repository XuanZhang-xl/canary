package com.xl.canary.handler.condition;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.exception.BaseException;
import org.springframework.stereotype.Component;

/**
 * 特殊的优惠券限制条件, 如果有多个特殊的条件, 则建多个实现即可
 * created by XUAN on 2018/09/07
 */
@Component("specialCouponConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.SPECIAL)
public class SpecialCouponConditionHandler implements ICouponConditionHandler{

    @Override
    public Boolean checkCondition(String target, String param, ArithmeticOperatorEnum operator) throws BaseException {
        return true;
    }
}
