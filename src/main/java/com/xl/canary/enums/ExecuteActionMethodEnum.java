package com.xl.canary.enums;

/**
 * 执行action的方式
 * created by XUAN on 2018/08/24
 */
public enum ExecuteActionMethodEnum {

    /**
     * 新开线程
     */
    NEW_THREAD(),

    /**
     * 以当前线程执行
     */
    CONTINUE(),

    ;

}
