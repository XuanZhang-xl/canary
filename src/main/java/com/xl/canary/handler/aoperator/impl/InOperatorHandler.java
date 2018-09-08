package com.xl.canary.handler.aoperator.impl;

import com.alibaba.fastjson.JSON;
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
 * in处理器
 * Created by xzhang on 2018/9/7.
 */
@Component("inOperatorHandler")
@ArithmeticOperatorHandler(operator = ArithmeticOperatorEnum.IN)
public class InOperatorHandler implements IArithmeticOperatorHandler {

    Logger logger = LoggerFactory.getLogger(InOperatorHandler.class);

    @Override
    public Boolean operate(String target, Comparable param) throws CompareException {
        if (StringUtils.isBlank(target) || param == null) {
            throw new CompareException("目标值[" + target + "] 或 参数[" + param + "] 不符合条件");
        }
        String strParam = param.toString();
        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(strParam);
        if (paramIsNumber) {
            List<BigDecimal> targetList = JSON.parseArray(target, BigDecimal.class);
            BigDecimal paramNumber = new BigDecimal(strParam);
            for (BigDecimal t : targetList) {
                if (paramNumber.compareTo(t) == 0) {
                    return true;
                }
            }
            return false;
        } else {
            List<String> targetList = JSON.parseArray(target, String.class);
            for (String str : targetList) {
                if (strParam.equals(str)) {
                    return true;
                }
            }
            return false;
        }
    }
}
