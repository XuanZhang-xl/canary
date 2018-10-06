package com.xl.canary.service;

import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.exception.CouponException;

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
     * 获得一张初始化优惠券
     * @param couponType     类型
     * @param weightAmount   金额/比例
     * @param effectiveDate  有效期开始
     * @param expireDate     失效日期
     * @return 优惠券
     * @throws CouponException 校验失败
     */
    CouponEntity getInitCoupon (CouponTypeEnum couponType, BigDecimal weightAmount, Long effectiveDate, Long expireDate) throws CouponException;


    /**
     * 将优惠券绑定给用户
     * @param couponId   优惠券id
     * @param userCode   用户
     */
    CouponEntity boundCouponToUser(String couponId, String userCode) throws CouponException;

    /**
     * 将优惠券绑定给订单(借款订单, 还款订单)
     * @param couponId   优惠券id
     * @param orderId    orderId / payOrderId
     */
    CouponEntity boundCouponToOrder(String couponId, String orderId) throws CouponException;

    /**
     * 保存优惠券
     * @param couponType     类型
     * @param weightAmount   数量
     * @param effectiveDate  有效期其实
     * @param expireDate     失效日期
     * @param condition      使用限制
     * @return  保存后的实体
     */
    CouponEntity saveCoupon(CouponTypeEnum couponType, BigDecimal weightAmount, Long effectiveDate, Long expireDate, String condition);

    /**
     * 检查优惠券是否可用
     * @param couponEntities
     * @return
     */
    Boolean checkCoupons(List<CouponEntity> couponEntities);

    /**
     * 批次号查询
     * @param couponIds
     * @return
     */
    List<CouponEntity> listByCouponBatchId(List<String> couponBatchIds);
}
