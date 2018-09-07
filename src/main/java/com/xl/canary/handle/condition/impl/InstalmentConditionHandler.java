package com.xl.canary.handle.condition.impl;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.handle.condition.CouponConditionHandler;
import com.xl.canary.handle.condition.DefaultCouponConditionHandler;
import org.springframework.stereotype.Component;

/**
 * 期数限定handler
 * Created by xzhang on 2018/9/7.
 */
@Component("instalmentConditionHandler")
@CouponConditionHandler(condition = CouponConditionEnum.INSTALMENT)
public class InstalmentConditionHandler extends DefaultCouponConditionHandler {
}
