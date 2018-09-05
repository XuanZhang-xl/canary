package com.xl.canary.service;

import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.enums.coupon.CouponTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xzhang on 2018/9/5.
 */
public interface CouponConditionService {

    /**
     * 根据优惠券类型查询
     * @param couponType  优惠券类型
     * @return
     */
    List<CouponConditionEntity> listByCouponType(String couponType);
}
