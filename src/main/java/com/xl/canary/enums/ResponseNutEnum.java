package com.xl.canary.enums;

/**
 * 错误码
 * 前4位代表项目
 * 后4位代表实际错误码
 * created by MSI-PC on 2018/01/13
 */
public enum ResponseNutEnum {
    OK(10000000, "成功", "成功"),

    ERROR_LAST_ELEMENT(10000001, "系统内部异常", "错误的入账最后一位入账顺序"),
    NO_USER(10000002, "用户不存在", "用户不存在"),
    LOCK_ERROR(10000003, "系统内部异常", "锁竞争失败"),



    UNKNOWN_EXCEPTION(10009999, "系统内部异常", "未知异常");


    ResponseNutEnum(Integer code, String message, String explanation) {
        this.code = code;
        this.message = message;
        this.explanation = explanation;
    }

    private Integer code;

    private String message;

    private String explanation;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExplanation() {
        return explanation;
    }

    public static ResponseNutEnum getByCode(Integer code) {
        for (ResponseNutEnum responseNutEnum : ResponseNutEnum.values()) {
            if (responseNutEnum.getCode().equals(code)) {
                return responseNutEnum;
            }
        }
        return null;
    }
}
