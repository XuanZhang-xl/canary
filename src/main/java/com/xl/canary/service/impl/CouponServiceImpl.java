package com.xl.canary.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.CouponCondition;
import com.xl.canary.entity.AbstractOrderEntity;
import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.entity.UserEntity;
import com.xl.canary.enums.ResponseNutEnum;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.UserActionEnum;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.exception.CouponException;
import com.xl.canary.exception.InnerException;
import com.xl.canary.mapper.CouponMapper;
import com.xl.canary.service.*;
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
    private UserService userService;

    @Autowired
    private LoanOrderService loanOrderService;

    @Autowired
    private PayOrderService payOrderService;

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
    public CouponEntity boundCouponToUser(String couponId, String userCode) throws CouponException {
        CouponEntity coupon = couponMapper.getByCouponId(couponId);
        if (coupon.getCouponState().equals(StateEnum.PENDING.name())) {
            throw new CouponException("不合法的优惠券绑定状态: " + coupon.getCouponState());
        }
        UserEntity user = userService.getByUserCode(userCode);
        if (user == null) {
            throw new InnerException(ResponseNutEnum.NO_USER);
        }
        coupon.setUserCode(userCode);
        couponMapper.insertSelective(coupon);
        return coupon;
    }

    @Override
    public CouponEntity boundCouponToOrder(String couponId, String orderId) throws CouponException {
        CouponEntity coupon = couponMapper.getByCouponId(couponId);
        if (coupon.getCouponState().equals(StateEnum.PENDING.name())) {
            throw new CouponException("不合法的优惠券绑定状态: " + coupon.getCouponState());
        }
        List<CouponConditionEntity> couponConditionEntities = couponConditionService.listByCouponType(coupon.getCouponType());
        UserActionEnum userAction = null;
        for (CouponConditionEntity couponCondition : couponConditionEntities) {
            if (couponCondition.getCondition().equals(CouponConditionEnum.OCCASION)) {
                // TODO:涉及运算符操作
                break;
            }
        }
        AbstractOrderEntity abstractOrder = null;
        if (userAction == null) {
            // 没有使用情况限制, 则不应该来绑定订单
            throw new CouponException("优惠券[" + couponId + "]类型为[" + coupon.getCouponType() + "]不应绑定订单");
        } else if (userAction == UserActionEnum.LOAN_ORDER) {
            abstractOrder = loanOrderService.getByOrderId(orderId);
        } else if (userAction == UserActionEnum.PAY_ORDER) {
            abstractOrder = payOrderService.getByPayOrderId(orderId);
        } else {
            throw new CouponException("不支持的用户操作类型: " + userAction.name());
        }
        if (abstractOrder == null) {
            throw new CouponException("订单类型[" + userAction.name() + "]中找不到订单[" + orderId + "]");
        }
        coupon.setUserCode(abstractOrder.getUserCode());
        coupon.setBoundOrderId(orderId);
        couponMapper.insertSelective(coupon);
        return coupon;
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
