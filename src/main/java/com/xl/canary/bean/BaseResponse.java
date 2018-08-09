package com.xl.canary.bean;

import com.xl.canary.enums.ResponseNutEnum;

/**
 * 基础返回对象
 * created by MSI-PC on 2018/01/13
 */
public class BaseResponse<T> {

    private Integer code;

    private String msg;

    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResponse() {
        this.code = ResponseNutEnum.OK.getCode();
        this.msg = ResponseNutEnum.OK.getMsg();
    }

    public BaseResponse buildSuccessResponse(T responseData) {
        this.setData(responseData);
        return this;
    }

    public BaseResponse buildFailedResponse(ResponseNutEnum responseNutEnum) {
        this.code = responseNutEnum.getCode();
        this.msg = responseNutEnum.getMsg();
        return this;
    }

    public BaseResponse buildFailedResponse(Integer code, String desc) {
        this.code = code;
        this.msg = desc;
        return this;
    }
}
