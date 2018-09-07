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
     * @return 限制
     */
    List<CouponConditionEntity> listByCouponType(String couponType);

    /**
     * 获取一条限制
     * @param couponType   优惠券类型
     * @param condition    限制类型
     * @return 限制
     */
    CouponConditionEntity getByCouponTypeAndCondition(String couponType, String condition);

    /**
     * 获取一部分限制
     * @param couponType    优惠券类型
     * @param conditions    限制类型
     * @return 限制
     */
    List<CouponConditionEntity> listByCouponTypeAndConditions(String couponType, List<String> conditions);
}
