package com.xl.canary.handle.condition;

import com.xl.canary.enums.coupon.CouponConditionEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CouponConditionHandler {

    CouponConditionEnum condition();
}
