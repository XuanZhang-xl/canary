package com.xl.canary.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.CouponCondition;
import com.xl.canary.entity.*;
import com.xl.canary.enums.*;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.exception.CouponException;
import com.xl.canary.exception.InnerException;
import com.xl.canary.mapper.CouponMapper;
import com.xl.canary.service.*;
import com.xl.canary.utils.EssentialConstance;
import com.xl.canary.utils.IDWorker;
import org.apache.commons.lang3.StringUtils;
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
        List<ConditionEntity> couponConditionEntities = couponConditionService.listByCouponType(SubjectEnum.COUPON.name(), couponType.name());
        List<CouponCondition> couponConditions = new ArrayList<CouponCondition>();
        for (ConditionEntity conditionEntity : couponConditionEntities) {
            couponConditions.add(new CouponCondition(conditionEntity.getCondition(), conditionEntity.getOperator(), conditionEntity.getTarget()));
        }
        return this.saveCoupon(couponType, weightAmount, effectiveDate, effectiveDays, JSONObject.toJSONString(couponConditions));
    }

    @Override
    public CouponEntity boundCouponToUser(String couponId, String userCode) throws CouponException {
        CouponEntity coupon = couponMapper.getByCouponId(couponId);
        if (coupon.getCouponState().equals(StateEnum.PENDING.name())) {
            throw new CouponException("不合法的优惠券绑定状态: " + coupon.getCouponState());
        }
        if (StringUtils.isNoneBlank(coupon.getUserCode())) {
            if (coupon.getUserCode().equals(userCode)) {
                return coupon;
            } else {
                throw new CouponException("优惠券绑定用户[" + userCode + "]时, " + "已绑定用户[" + coupon.getUserCode() + "]");
            }
        }
        // TODO: 如果以后增加主体, 这里也要加入主体检查
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
        CouponTypeEnum couponType = CouponTypeEnum.valueOf(coupon.getCouponType());
        if (!SubjectEnum.LOAN_ORDER.equals(couponType.getSubject())) {
            throw new CouponException("优惠券[" + couponId + "]可绑定主体为[" + couponType.getSubject().name() + "], 不可绑定订单");
        }
        LoanOrderEntity loanOrder = loanOrderService.getByOrderId(orderId);
        if (loanOrder == null) {
            throw new CouponException("找不到借款订单[" + orderId + "]");
        }
        if (StringUtils.isNoneBlank(coupon.getUserCode())) {
            if (!coupon.getUserCode().equals(loanOrder.getUserCode())) {
                throw new CouponException("优惠券绑定用户[" + loanOrder.getUserCode() + "]时, " + "已绑定用户[" + coupon.getUserCode() + "]");
            }
        }
        BigDecimal equivalentAmount = loanOrder.getEquivalentAmount();
        BigDecimal applyAmount;
        if (WeightEnum.PERCENT.equals(couponType.getWeight())) {
            applyAmount = equivalentAmount.multiply(coupon.getDefaultAmount());
        } else {
            applyAmount = coupon.getDefaultAmount();
        }
        coupon.setApplyAmount(applyAmount);
        coupon.setUserCode(loanOrder.getUserCode());
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
