package com.xl.canary.service;

import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.coupon.CouponTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xzhang on 2018/9/5.
 */
public interface CouponService {

    /**
     * 根据优惠券类型查询
     * @param couponId  优惠券id
     * @return
     */
    CouponEntity getByCouponId(String couponId);

    /**
     * 保存优惠券
     * @param couponType     类型
     * @param weightAmount   数量
     * @param effectiveDate  有效期其实
     * @param effectiveDays  有效天数
     * @param condition      使用限制
     * @return  保存后的实体
     */
    CouponEntity saveCoupon(CouponTypeEnum couponType, BigDecimal weightAmount, Long effectiveDate, Integer effectiveDays, String condition);
}
