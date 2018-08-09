package com.xl.canary.exception;


import com.xl.canary.enums.ResponseNutEnum;
import com.xl.canary.exception.BasicException;

/**
 * 当创建异常烦的时候就抛这个异常
 * created by XUAN on 2018/01/15
 */
public class GeneralException extends BasicException {
    public GeneralException(ResponseNutEnum responseNutEnum) {
        super(responseNutEnum);
    }
}
