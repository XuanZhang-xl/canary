package com.xl.canary.handler.aoperator.impl;

import com.alibaba.fastjson.JSON;
import com.xl.canary.controller.LoanOrderController;
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

    Logger logger = LoggerFactory.getLogger(BetweenOperatorHandler.class);

    @Override
    public Boolean operate(String target, String param) throws CompareException {
        if (StringUtils.isBlank(target) || StringUtils.isBlank(param)) {
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

        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(param);
        if (paramIsNumber) {
            List<BigDecimal> targetList = JSON.parseArray(target, BigDecimal.class);
            if (targetList == null || targetList.size() != 2) {
                throw new CompareException("BETWEEN 操作符目标参数应有且仅有两个参数! 当前参数: " + target);
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
            Boolean begin;
            Boolean end;
            if (containBegin) {
                begin = paramNumber.compareTo(min) >= 0;
            } else  {
                begin = paramNumber.compareTo(min) > 0;
            }
            if (containEnd) {
                end = paramNumber.compareTo(max) <= 0;
            } else {
                end = paramNumber.compareTo(max) < 0;
            }
            return begin && end;
        } else {
            logger.warn("目标值[{}], 参数[{}], 校验不通过", target, param);
            return false;
        }
    }
}
