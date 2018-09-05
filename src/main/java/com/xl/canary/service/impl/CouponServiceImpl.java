package com.xl.canary.service.impl;

import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.mapper.CouponMapper;
import com.xl.canary.service.CouponService;
import com.xl.canary.utils.IDWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by xzhang on 2018/9/5.
 */
@Service("couponServiceImpl")
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private IDWorker idWorker;

    @Value("${equivalent.currency}")
    private String equivalent;

    @Override
    public CouponEntity getByCouponId(String couponId) {
        return couponMapper.getByCouponId(couponId);
    }

    @Override
    public CouponEntity saveCoupon(CouponTypeEnum couponType, BigDecimal weightAmount, Long effectiveDate, Integer effectiveDays, String condition) {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponId(String.valueOf(idWorker.nextId()));
        couponEntity.setCouponType(CouponTypeEnum.INTEREST_COUPON.name());
        couponEntity.setCouponState(StateEnum.PENDING.name());
        couponEntity.setEquivalent(equivalent);
        couponEntity.setEffectiveDate(effectiveDate);
        couponEntity.setEffectiveDays(effectiveDays);
        couponEntity.setDefaultAmount(weightAmount);
        couponEntity.setCondition(condition);
        couponMapper.insertSelective(couponEntity);
        return couponEntity;
    }
}
