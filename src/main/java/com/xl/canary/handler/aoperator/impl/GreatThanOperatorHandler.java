package com.xl.canary.handler.aoperator.impl;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.CompareException;
import com.xl.canary.handler.aoperator.ArithmeticOperatorHandler;
import com.xl.canary.handler.aoperator.IArithmeticOperatorHandler;
import com.xl.canary.utils.ArithmeticOperatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 不等于号处理器
 * Created by xzhang on 2018/9/7.
 */
@Component("greatThanOperatorHandler")
@ArithmeticOperatorHandler(operator = ArithmeticOperatorEnum.GREAT_THAN)
public class GreatThanOperatorHandler implements IArithmeticOperatorHandler {

    @Override
    public Boolean operate(String target, Comparable param) throws CompareException {
        if (StringUtils.isBlank(target) || param == null) {
            throw new CompareException("目标值[" + target + "] 或 参数[" + param + "] 不符合条件");
        }
        String strParam = param.toString();
        boolean targetIsNumber = ArithmeticOperatorUtils.isNumber(target);
        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(strParam);
        if (targetIsNumber && paramIsNumber) {
            // 都是数字
            BigDecimal targetNumber = new BigDecimal(target);
            BigDecimal paramNumber = new BigDecimal(strParam);
            return targetNumber.compareTo(paramNumber) < 0;
        } else {
            throw new CompareException("目标值[" + target + "] 参数[" + strParam + "] 不可比较!");
        }
    }
}