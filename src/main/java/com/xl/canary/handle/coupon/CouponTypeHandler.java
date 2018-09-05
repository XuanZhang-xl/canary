package com.xl.canary.handle.coupon;

import com.xl.canary.enums.coupon.CouponTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CouponTypeHandler {

    CouponTypeEnum type();
}
