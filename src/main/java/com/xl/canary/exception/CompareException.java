package com.xl.canary.exception;

/**
 * Created by xzhang on 2018/9/7.
 */
public class CompareException extends BaseException {
    public CompareException(String msg) {
        super(msg);
    }

    public CompareException(Throwable t) {
        super(t);
    }

    public CompareException(String msg, Throwable t) {
        super(msg, t);
    }
}
