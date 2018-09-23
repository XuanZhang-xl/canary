package com.xl.canary.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xl.canary.bean.structure.Schema;
import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.engine.calculate.siuation.SituationHolder;
import com.xl.canary.engine.calculate.siuation.SituationSource;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class SituationAspect {

    private static final Logger logger = LoggerFactory.getLogger(SituationAspect.class);

    @Around("@annotation(situationSource)")
    public Object changeDataSource(ProceedingJoinPoint point, SituationSource situationSource) throws Throwable{
        logger.info("进入[SituationAspect]");
        Object[] args = point.getArgs();
        Situation situation = SituationHolder.getSituation();
        for (Object arg : args) {
            if (arg instanceof LoanOrderEntity) {
                LoanOrderEntity order = (LoanOrderEntity) arg;
                situation.collect(CouponConditionEnum.LOAN_AMOUNT, order.getEquivalentAmount());
                situation.collect(CouponConditionEnum.LOAN_CURRENCY, order.getEquivalent());
            }
            if (arg instanceof PayOrderEntity) {
                PayOrderEntity order = (PayOrderEntity) arg;
                situation.collect(CouponConditionEnum.PAY_AMOUNT, order.getEquivalentAmount());
                situation.collect(CouponConditionEnum.PAY_CURRENCY, order.getEquivalent());
            }
        }
        return point.proceed();
    }

}
