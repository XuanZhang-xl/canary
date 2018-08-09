package com.xl.canary.enums;

/**
 * 错误码
 * created by MSI-PC on 2018/01/13
 */
public enum ResponseNutEnum {
    OK(0, "成功"),

    PRODUCT_NOT_FIND(1000, "未找到对应商品"),
    PRODUCT_INSUFFICIENT(1001, "商品库存不足, productId:"),
    PRODUCT_DETAIL_NOT_FIND(1002, "商品详情未找到"),
    ORDER_STATUS_ERROR(1003, "订单状态修改异常"),
    ORDER_PAY_STATUS_ERROR(1004, "订单支付状态不正确"),
    PARAM_ERROR(1005, "参数检验遗常"),
    CART_EMPTY(1006, "购物车不能为空"),
    ORDER_NOT_FIND(1006, "未找到对应订单"),
    NO_ORDER_UNDER_OPENID(1007, "对应openid下无此 订单"),



    UNKNOWN_EXCEPTION(9999, "未知异常"),
    ;


    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    ResponseNutEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
