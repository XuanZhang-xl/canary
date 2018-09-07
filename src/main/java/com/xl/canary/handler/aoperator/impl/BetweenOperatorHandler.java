package com.xl.canary.handler.aoperator.impl;

import com.alibaba.fastjson.JSON;
import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.CompareException;
import com.xl.canary.handler.aoperator.ArithmeticOperatorHandler;
import com.xl.canary.handler.aoperator.IArithmeticOperatorHandler;
import com.xl.canary.utils.ArithmeticOperatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 两个数字之间处理器
 * 边界问题:
 * [ : 包括开始
 * ( : 不包括开始
 * ] : 包括结束
 * ( ： 不包括结束
 * Created by xzhang on 2018/9/7.
 */
@Component("betweenOperatorHandler")
@ArithmeticOperatorHandler(operator = ArithmeticOperatorEnum.BETWEEN)
public class BetweenOperatorHandler implements IArithmeticOperatorHandler {

    @Override
    public Boolean operate(String target, Comparable param) throws CompareException {
        if (StringUtils.isBlank(target) || param == null) {
            throw new CompareException("目标值[" + target + "] 或 参数[" + param + "] 不符合条件");
        }

        // 处理边界问题
        Boolean containBegin;
        Boolean containEnd;
        target = target.trim();
        if (target.startsWith("[")) {
            containBegin = true;
        } else if (target.startsWith("(")) {
            containBegin = false;
            target = new StringBuilder(target).replace(0, 1, "[").toString();
        } else {
            throw new CompareException("目标值[" + target + "] 错误, 必须以 '[' 或 '(' 开头");
        }
        if (target.endsWith("]")) {
            containEnd = true;
        } else if (target.endsWith(")")) {
            containEnd = false;
            target = new StringBuilder(target).replace(target.length() - 1, target.length(), "]").toString();
        } else {
            throw new CompareException("目标值[" + target + "] 错误, 必须以 ']' 或 ')' 结束");
        }
        String strParam = param.toString();
        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(strParam);
        if (paramIsNumber) {
            List<BigDecimal> targetList = JSON.parseArray(target, BigDecimal.class);
            if (targetList == null || targetList.size() != 2) {
                throw new CompareException("BETWEEN 操作符目标参数应有且仅有两个参数! 当前参数: " + target);
            }
            BigDecimal paramNumber = new BigDecimal(strParam);
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
