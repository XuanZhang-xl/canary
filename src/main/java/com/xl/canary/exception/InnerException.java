package com.xl.canary.exception;


import com.xl.canary.enums.ResponseNutEnum;

/**
 * 一般不会出现的异常, 如果出现, 表示代码需要修正. 比如枚举找不到等, 外面不需要关心
 * created by XUAN on 2018/01/15
 */
public class InnerException extends ServiceException {
    public InnerException(ResponseNutEnum responseNutEnum) {
        super(responseNutEnum);
    }
}
