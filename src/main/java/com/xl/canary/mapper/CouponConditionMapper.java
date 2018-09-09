package com.xl.canary.mapper;

import com.xl.canary.entity.ConditionEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * created by XUAN on 2018/08/20
 */
@org.apache.ibatis.annotations.Mapper
public interface CouponConditionMapper extends Mapper<ConditionEntity>{

    /**
     * 根据优惠券类型查询
     * @param couponType  优惠券类型
     * @return  限制
     */
    List<ConditionEntity> listByCouponType(String subject, String couponType);

    /**
     * 获取一条限制
     * @param couponType   优惠券类型
     * @param condition    限制类型
     * @return 限制
     */
    ConditionEntity getByCouponTypeAndCondition(String subject, String couponType, String condition);

    /**
     * 获取一部分限制
     * @param couponType    优惠券类型
     * @param conditions    限制类型
     * @return 限制
     */
    List<ConditionEntity> listByCouponTypeAndConditions(String subject, String couponType, List<String> conditions);
}
