package com.xl.canary.aspect;

import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.engine.calculate.siuation.SituationHolder;
import com.xl.canary.engine.calculate.siuation.SituationSource;
import com.xl.canary.entity.LoanInstalmentEntity;
import com.xl.canary.entity.LoanOrderEntity;
import com.xl.canary.entity.PayOrderEntity;
import com.xl.canary.enums.coupon.CouponConditionEnum;
import com.xl.canary.service.LoanOrderService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class SituationAspect {

    private static final Logger logger = LoggerFactory.getLogger(SituationAspect.class);

    @Autowired
    private LoanOrderService loanOrderService;

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
            if (arg instanceof List) {
                String orderId = null;
                List list = (List) arg;
                for (Object o : list) {
                    if (o instanceof LoanInstalmentEntity) {
                        LoanInstalmentEntity loanInstalment = (LoanInstalmentEntity) o;
                        orderId = loanInstalment.getOrderId();
                    }
                }
                if (StringUtils.isNotBlank(orderId)) {
                    LoanOrderEntity order = loanOrderService.getByOrderId(orderId);
                    situation.collect(CouponConditionEnum.LOAN_AMOUNT, order.getEquivalentAmount());
                    situation.collect(CouponConditionEnum.LOAN_CURRENCY, order.getEquivalent());
                }
            }
        }
        // 返回的结果已经使用完situation, 在这儿加入变量毫无意义
        Object proceed = point.proceed();
        // 清除threadLocal
        SituationHolder.clear();
        return proceed;
    }
}
