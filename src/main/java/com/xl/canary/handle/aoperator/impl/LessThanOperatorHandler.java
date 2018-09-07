package com.xl.canary.handle.aoperator.impl;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.CompareException;
import com.xl.canary.handle.aoperator.ArithmeticOperator;
import com.xl.canary.handle.aoperator.IArithmeticOperator;
import com.xl.canary.utils.ArithmeticOperatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 小于号处理器
 * Created by xzhang on 2018/9/7.
 */
@Component("lessThanOperatorHandler")
@ArithmeticOperator(operator = ArithmeticOperatorEnum.LESS_THAN)
public class LessThanOperatorHandler implements IArithmeticOperator {

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
            return targetNumber.compareTo(paramNumber) > 0;
        } else {
            throw new CompareException("目标值[" + target + "] 参数[" + param + "] 不可比较!");
        }
    }
}
