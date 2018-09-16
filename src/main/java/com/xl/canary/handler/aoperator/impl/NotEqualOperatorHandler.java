package com.xl.canary.handler.aoperator.impl;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.CompareException;
import com.xl.canary.handler.aoperator.ArithmeticOperatorHandler;
import com.xl.canary.handler.aoperator.IArithmeticOperatorHandler;
import com.xl.canary.utils.ArithmeticOperatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 不等于号处理器
 * Created by xzhang on 2018/9/7.
 */
@Component("notEqualOperatorHandler")
@ArithmeticOperatorHandler(operator = ArithmeticOperatorEnum.NOT_EQUAL)
public class NotEqualOperatorHandler implements IArithmeticOperatorHandler {

    Logger logger = LoggerFactory.getLogger(NotEqualOperatorHandler.class);

    @Override
    public Boolean operate(String target, String param) throws CompareException {
        if (StringUtils.isBlank(target) || StringUtils.isBlank(param)) {
            throw new CompareException("目标值[" + target + "] 或 参数[" + param + "] 不符合条件");
        }
        boolean targetIsNumber = ArithmeticOperatorUtils.isNumber(target);
        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(param);
        if (targetIsNumber && paramIsNumber) {
            // 都是数字
            BigDecimal targetNumber = new BigDecimal(target);
            BigDecimal paramNumber = new BigDecimal(param);
            return targetNumber.compareTo(paramNumber) != 0;
        } else if (!(targetIsNumber || paramIsNumber)) {
            return !target.trim().equals(param);
        } else {
            return true;
        }
    }
}
