package com.xl.canary.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.CouponCondition;
import com.xl.canary.entity.CouponConditionEntity;
import com.xl.canary.entity.CouponEntity;
import com.xl.canary.enums.StateEnum;
import com.xl.canary.enums.coupon.CouponTypeEnum;
import com.xl.canary.mapper.CouponConditionMapper;
import com.xl.canary.service.CouponConditionService;
import com.xl.canary.utils.IDWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
}
