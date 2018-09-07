package com.xl.canary.handle.condition;

import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.exception.NotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券条件工厂
 * Created by xzhang on 2018/9/7.
 */
@Component("couponConditionFactory")
public class CouponConditionFactory {

    private static final Logger logger = LoggerFactory.getLogger(CouponConditionFactory.class);

    @Autowired
    private ApplicationContext springContext;

    private static Map<CouponConditionEnum, ICouponConditionHandler> couponConditionHandlers = new HashMap<CouponConditionEnum, ICouponConditionHandler>();

    @PostConstruct
    public void init() {
        Collection states = this.springContext.getBeansWithAnnotation(CouponConditionHandler.class).values();
        for (Object object : states) {
            if (!(object instanceof ICouponConditionHandler)) {
                continue;
            }
            ICouponConditionHandler couponConditionHandler = (ICouponConditionHandler) object;
            CouponConditionHandler annotation = couponConditionHandler.getClass().getAnnotation(CouponConditionHandler.class);
            couponConditionHandlers.put(annotation.condition(), couponConditionHandler);
            logger.info("优惠券条件：[{}]，处理类：[{}]",annotation.condition().name(), couponConditionHandler.getClass());
        }
    }

    public static ICouponConditionHandler instance (CouponConditionEnum condition) throws NotExistException {
        ICouponConditionHandler couponConditionHandler = couponConditionHandlers.get(condition);
        if (couponConditionHandler == null) {
            logger.debug("优惠券条件[{}]没有匹配的处理类", condition.name());
            throw new NotExistException("优惠券条件"+ condition.name() + "没有匹配的处理类");
        } else {
            return couponConditionHandler;
        }
    }
}
