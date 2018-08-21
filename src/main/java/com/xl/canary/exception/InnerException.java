package com.xl.canary.exception;


import com.xl.canary.enums.ResponseNutEnum;

/**
 * 当创建异常烦的时候就抛这个异常
 * created by XUAN on 2018/01/15
 */
public class InnerException extends ServiceException {
    public InnerException(ResponseNutEnum responseNutEnum) {
        super(responseNutEnum);
    }
}
