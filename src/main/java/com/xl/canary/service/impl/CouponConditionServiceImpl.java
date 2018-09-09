package com.xl.canary.service.impl;

import com.xl.canary.entity.ConditionEntity;
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
    public List<ConditionEntity> listByCouponType(String subject, String couponType) {
        return couponConditionMapper.listByCouponType(subject, couponType);
    }

    @Override
    public ConditionEntity getByCouponTypeAndCondition(String subject, String couponType, String condition) {
        return couponConditionMapper.getByCouponTypeAndCondition(subject, couponType, condition);
    }

    @Override
    public List<ConditionEntity> listByCouponTypeAndConditions(String subject, String couponType, List<String> conditions) {
        return couponConditionMapper.listByCouponTypeAndConditions(subject, couponType, conditions);
    }
}
