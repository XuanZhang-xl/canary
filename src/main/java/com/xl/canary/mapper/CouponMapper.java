package com.xl.canary.mapper;

import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.PayOrderEntity;
import tk.mybatis.mapper.common.Mapper;

/**
 * created by XUAN on 2018/08/20
 */
@org.apache.ibatis.annotations.Mapper
public interface CouponMapper extends Mapper<CouponEntity>{

    /**
     * 根据优惠券号查询
     * @param couponId
     * @return
     */
    CouponEntity getByCouponId(String couponId);
}
