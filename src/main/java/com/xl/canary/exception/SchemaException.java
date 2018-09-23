package com.xl.canary.exception;

/**
 * Created by gqwu on 2018/4/4.
 */
public class SchemaException extends BaseException {
    public SchemaException(String msg) {
        super(msg);
    }

    public SchemaException(Throwable t) {
        super(t);
    }

    public SchemaException(String msg, Throwable t) {
        super(msg, t);
    }
}
