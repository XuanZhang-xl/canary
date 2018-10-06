package com.xl.canary.mapper;

import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.PayOrderEntity;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

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

    /**
     * 批次号查询
     * @param couponBatchIds
     * @return
     */
    List<CouponEntity> listByCouponBatchId(@Param("couponBatchIds") List<String> couponBatchIds);
}
