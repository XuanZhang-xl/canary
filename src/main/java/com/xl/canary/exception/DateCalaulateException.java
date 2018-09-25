package com.xl.canary.exception;


/**
 * Created by gqwu on 2018/4/4.
 */
public class DateCalaulateException extends BaseException {
    public DateCalaulateException(String msg) {
        super(msg);
    }

    public DateCalaulateException(Throwable t) {
        super(t);
    }

    public DateCalaulateException(String msg, Throwable t) {
        super(msg, t);
    }
}
