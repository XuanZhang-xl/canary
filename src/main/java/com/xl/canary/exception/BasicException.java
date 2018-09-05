package com.xl.canary.exception;


import com.xl.canary.enums.ResponseNutEnum;

/**
 * created by XUAN on 2018/01/14
 */
public class BasicException extends Exception {

    private ResponseNutEnum responseNutEnum;

    public ResponseNutEnum getResponseNutEnum() {
        return responseNutEnum;
    }

    public void setResponseNutEnum(ResponseNutEnum responseNutEnum) {
        this.responseNutEnum = responseNutEnum;
    }

    public BasicException(ResponseNutEnum responseNutEnum) {
        super(responseNutEnum.getMessage());
        this.responseNutEnum = responseNutEnum;
    }

    public BasicException(Integer code, String msg) {
        ResponseNutEnum responseNutEnum = ResponseNutEnum.getByCode(code);
        responseNutEnum.setMessage(msg);
        this.responseNutEnum = responseNutEnum;
    }

}
