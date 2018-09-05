package com.xl.canary.mapper;

import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.PayOrderEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * created by XUAN on 2018/08/20
 */
@org.apache.ibatis.annotations.Mapper
public interface CouponConditionMapper extends Mapper<CouponConditionEntity>{

    /**
     * 根据优惠券类型查询
     * @param couponType  优惠券类型
     * @return
     */
    List<CouponConditionEntity> listByCouponType(String couponType);
}
