package com.xl.canary.handle.condition;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.handle.aoperator.ArithmeticOperatorFactory;
import com.xl.canary.handle.aoperator.IArithmeticOperator;

/**
 * 默认处理类
 * Created by xzhang on 2018/9/7.
 */
public abstract class DefaultCouponConditionHandler implements ICouponConditionHandler {

    @Override
    public Boolean checkCondition(String target, String param, ArithmeticOperatorEnum operator) throws BaseException {
        IArithmeticOperator instance = ArithmeticOperatorFactory.instance(operator);
        return instance.operate(target, param);
    }
}
