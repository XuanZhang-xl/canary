package com.xl.canary.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.CouponCondition;
import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.exception.CouponException;
import com.xl.canary.mapper.CouponMapper;
import com.xl.canary.service.CouponConditionService;
import com.xl.canary.service.CouponService;
import com.xl.canary.utils.EssentialConstance;
import com.xl.canary.utils.IDWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xzhang on 2018/9/5.
 */
@Service("couponServiceImpl")
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponConditionService couponConditionService;

    @Autowired
    private IDWorker idWorker;

    @Value("${equivalent.currency}")
    private String equivalent;

    @Override
    public CouponEntity getByCouponId(String couponId) {
        return couponMapper.getByCouponId(couponId);
    }

    @Override
    public CouponEntity getInitCoupon(CouponTypeEnum couponType, BigDecimal weightAmount, Long effectiveDate, Integer effectiveDays) throws CouponException {
        if (weightAmount == null || weightAmount.compareTo(BigDecimal.ZERO) < 0 || weightAmount.compareTo(BigDecimal.ONE) > 0) {
            throw new CouponException("不合法的优惠比例: " + (weightAmount == null ? "优惠比例为空" : weightAmount.toPlainString()));
        }
        long now = System.currentTimeMillis();
        long effectiveDateEnd = effectiveDate + effectiveDays * EssentialConstance.DAY_MILLISECOND;
        if (effectiveDateEnd < now) {
            throw new CouponException("不合法的优惠结束时间: " + effectiveDateEnd);
        }

        // 使用限制
        List<CouponConditionEntity> couponConditionEntities = couponConditionService.listByCouponType(couponType.name());
        List<CouponCondition> couponConditions = new ArrayList<CouponCondition>();
        for (CouponConditionEntity couponConditionEntity : couponConditionEntities) {
            couponConditions.add(new CouponCondition(couponConditionEntity.getCondition(), couponConditionEntity.getOperator(), couponConditionEntity.getValue()));
        }
        return this.saveCoupon(couponType, weightAmount, effectiveDate, effectiveDays, JSONObject.toJSONString(couponConditions));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
