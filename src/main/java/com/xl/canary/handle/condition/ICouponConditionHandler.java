package com.xl.canary.handle.condition;

import com.xl.canary.enums.ArithmeticOperatorEnum;
import com.xl.canary.exception.BaseException;

/**
 * 优惠券限制条件处理器
 * Created by xzhang on 2018/9/7.
 */
public interface ICouponConditionHandler {

    /**
     * 检查条件是否通过
     * @param target    目标值
     * @param param     当前值
     * @param operator  运算符
     * @return
     */
    Boolean checkCondition(String target, String param, ArithmeticOperatorEnum operator) throws BaseException;
}
