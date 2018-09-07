package com.xl.canary.handle.aoperator.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
 * in处理器
 * Created by xzhang on 2018/9/7.
 */
@Component("inOperatorHandler")
@ArithmeticOperator(operator = ArithmeticOperatorEnum.IN)
public class InOperatorHandler implements IArithmeticOperator {

    @Override
    public Boolean operate(String target, String param) throws CompareException {
        if (StringUtils.isBlank(target) || StringUtils.isBlank(param)) {
            throw new CompareException("目标值[" + target + "] 或 参数[" + param + "] 不符合条件");
        }

        boolean paramIsNumber = ArithmeticOperatorUtils.isNumber(param);
        if (paramIsNumber) {
            List<BigDecimal> targetList = JSON.parseArray(target, BigDecimal.class);
            BigDecimal paramNumber = new BigDecimal(param);
            for (BigDecimal t : targetList) {
                if (paramNumber.compareTo(t) == 0) {
                    return true;
                }
            }
            return false;
        } else {
            String paramTrim = param.trim();
            List<String> targetList = JSON.parseArray(target, String.class);
            for (String str : targetList) {
                if (paramTrim.equals(str)) {
                    return true;
                }
            }
            return false;
        }
    }
}
