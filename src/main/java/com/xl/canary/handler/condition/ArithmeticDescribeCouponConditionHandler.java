package com.xl.canary.handler.condition;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.BaseException;
import com.xl.canary.handler.aoperator.ArithmeticOperatorHandlerFactory;
import com.xl.canary.handler.aoperator.IArithmeticOperatorHandler;

/**
 * 默认处理类
 * Created by xzhang on 2018/9/7.
 */
public abstract class ArithmeticDescribeCouponConditionHandler implements ICouponConditionHandler {

    @Override
    public Boolean checkCondition(String target, Comparable param, ArithmeticOperatorEnum operator) throws BaseException {
        IArithmeticOperatorHandler instance = ArithmeticOperatorHandlerFactory.instance(operator);
        return instance.operate(target, param);
    }
}
