package com.xl.canary.handle.coupon;

import com.xl.canary.entity.AbstractOrderEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.WeightEnum;
import com.xl.canary.exception.CouponException;

import java.math.BigDecimal;

/**
 * 优惠券条件处理类
 * Created by xzhang on 2018/9/5.
 */
public interface ICouponTypeHandler {

    /**
     * 获取一张初始化的优惠券
     * @param weightAmount       比重数量
     * @param effectiveDate      有效起始日
     * @param effectiveDays      有效天数
     * @return 一张优惠券
     */
    CouponEntity getInitCoupon(BigDecimal weightAmount, Long effectiveDate, Integer effectiveDays) throws CouponException;

    /**
     * 检查优惠券是否可用
     * @param orderEntity   订单
     * @return  是否可用
     */
    Boolean checkConditions(AbstractOrderEntity orderEntity);

}
