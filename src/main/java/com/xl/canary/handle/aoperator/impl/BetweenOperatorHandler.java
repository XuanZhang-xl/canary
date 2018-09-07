package com.xl.canary.handle.aoperator.impl;

import com.alibaba.fastjson.JSON;
import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.CompareException;
import com.xl.canary.handle.aoperator.ArithmeticOperator;
import com.xl.canary.handle.aoperator.IArithmeticOperator;
import com.xl.canary.utils.ArithmeticOperatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 两个数字之间处理器
 * TODO: 边界问题怎么解决?
 * Created by xzhang on 2018/9/7.
 */
@Component("betweenOperatorHandler")
@ArithmeticOperator(operator = ArithmeticOperatorEnum.BETWEEN)
public class BetweenOperatorHandler implements IArithmeticOperator {

    @Override
    public Boolean operate(String target, String param) throws CompareException {
        if (StringUtils.isBlank(target) || StringUtils.isBlank(param)) {
            throw new CompareException("目标值[" + target + "] 或 参数[" + param + "] 不符合条件");
        }

        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(param);
        if (paramIsNumber) {
            List<BigDecimal> targetList = JSON.parseArray(target, BigDecimal.class);
            if (targetList == null || targetList.size() < 2) {
                throw new CompareException("BETWEEN 操作符至少应该包含两个参数! 当前参数: " + target);
            }
            BigDecimal paramNumber = new BigDecimal(param);
            BigDecimal min = null;
            BigDecimal max = null;
            for (BigDecimal number : targetList) {
                if (min == null || min.compareTo(number) > 0) {
                    min = number;
                }
                if (max == null || max.compareTo(number) < 0) {
                    max = number;
                }
            }
            return paramNumber.compareTo(min) >= 0 && paramNumber.compareTo(max) <= 0;
        } else {
            return false;
        }
    }
}
