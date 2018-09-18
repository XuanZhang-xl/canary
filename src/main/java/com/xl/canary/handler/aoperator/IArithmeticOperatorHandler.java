package com.xl.canary.handler.aoperator;

import com.xl.canary.exception.CompareException;

/**
 * 数学操作符抽象接口
 * Created by xzhang on 2018/9/7.
 */
public interface IArithmeticOperatorHandler {

    /**
     * 操作的实际实现
     * @param target  目标值
     * @param param   当前实际值
     * @return        是否通过校验
     */
    Boolean operate(String target, String param) throws CompareException;
}
