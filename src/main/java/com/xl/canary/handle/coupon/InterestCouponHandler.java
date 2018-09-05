package com.xl.canary.handle.coupon;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.CouponCondition;
import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.entity.AbstractOrderEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.exception.CouponException;
import com.xl.canary.service.CouponConditionService;
import com.xl.canary.service.CouponService;
import com.xl.canary.utils.EssentialConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 利息优惠券处理类
 * TODO: 直接写service里好了
 * Created by xzhang on 2018/9/5.
 */
@Component("interestCouponHandler")
@CouponTypeHandler(type = CouponTypeEnum.INTEREST_COUPON)
public class InterestCouponHandler implements ICouponTypeHandler {

    @Autowired
    private CouponConditionService couponConditionService;

    @Autowired
    private CouponService couponService;

    /**
     * 利息优惠券限制条件总和
     */
    public static final List<CouponConditionEnum> INTEREST_COUPON_CONDITIONS = Arrays.asList(
            CouponConditionEnum.OCCASION,
            CouponConditionEnum.PAY_AMOUNT,
            CouponConditionEnum.PAY_CURRENCY);

    @Override
    public CouponEntity getInitCoupon (BigDecimal weightAmount, Long effectiveDate, Integer effectiveDays) throws CouponException {
        if (weightAmount == null || weightAmount.compareTo(BigDecimal.ZERO) < 0 || weightAmount.compareTo(BigDecimal.ONE) > 0) {
            throw new CouponException("不合法的优惠比例: " + (weightAmount == null ? "优惠比例为空" : weightAmount.toPlainString()));
        }
        long now = System.currentTimeMillis();
        long effectiveDateEnd = effectiveDate + effectiveDays * EssentialConstance.DAY_MILLISECOND;
        if (effectiveDateEnd < now) {
            throw new CouponException("不合法的优惠结束时间: " + effectiveDateEnd);
        }

        // 使用限制
        List<CouponConditionEntity> couponConditionEntities = couponConditionService.listByCouponType(CouponTypeEnum.INTEREST_COUPON.name());
        List<CouponCondition> couponConditions = new ArrayList<CouponCondition>();
        for (CouponConditionEntity couponConditionEntity : couponConditionEntities) {
            couponConditions.add(new CouponCondition(couponConditionEntity.getCondition(), couponConditionEntity.getOperator(), couponConditionEntity.getValue()));
        }
        return couponService.saveCoupon(CouponTypeEnum.INTEREST_COUPON, weightAmount, effectiveDate, effectiveDays, JSONObject.toJSONString(couponConditions));
    }

    @Override
    public Boolean checkConditions(AbstractOrderEntity orderEntity) {
        return null;
    }
}
