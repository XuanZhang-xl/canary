package com.xl.canary.exception;

/**
 * Created by xzhang on 2018/9/5.
 */
public class CouponException extends BaseException {
    public CouponException(String msg) {
        super(msg);
    }

    public CouponException(Throwable t) {
        super(t);
    }

    public CouponException(String msg, Throwable t) {
        super(msg, t);
    }
}
