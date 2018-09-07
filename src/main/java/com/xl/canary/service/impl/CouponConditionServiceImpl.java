package com.xl.canary.service.impl;

import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.mapper.CouponConditionMapper;
import com.xl.canary.service.CouponConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xzhang on 2018/9/5.
 */
@Service("couponConditionServiceImpl")
public class CouponConditionServiceImpl implements CouponConditionService {

    @Autowired
    private CouponConditionMapper couponConditionMapper;

    @Override
    public List<CouponConditionEntity> listByCouponType(String couponType) {
        return couponConditionMapper.listByCouponType(couponType);
    }

    @Override
    public CouponConditionEntity getByCouponTypeAndCondition(String couponType, String condition) {
        return couponConditionMapper.getByCouponTypeAndCondition(couponType, condition);
    }

    @Override
    public List<CouponConditionEntity> listByCouponTypeAndConditions(String couponType, List<String> conditions) {
        return couponConditionMapper.listByCouponTypeAndConditions(couponType, conditions);
    }
}
