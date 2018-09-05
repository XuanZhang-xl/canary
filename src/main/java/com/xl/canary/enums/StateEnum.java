package com.xl.canary.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Created by XUAN on 2018/9/04.
 */
public enum StateEnum {

    /**
     * 通用状态
     */
    PENDING("排队中"),
    AUDITING("审核中"),
    PASSED("审核通过"),
    REJECTED("已拒绝"),
    EXPIRED("已超时"),
    CANCELLED("已取消"),

    /**
     * 贷款订单状态
     */
    LENDING("放款中"),
    LENT("已放款"),
    FAILED("放款失败"),
    PAYOFF("已还清"),
    LIQUIDATED_BROKE("已破产"),

    /**
     * 还款订单状态
     */
    DEDUCTING("扣款中"),
    DEDUCTED("已扣款"),
    DEDUCT_FAILED("扣款失败"),
    ENTRY_DOING("入账中, 后面会删掉"),
    ENTRY_DONE("已入账"),
    ENTRY_FAILED("入账失败"),
    REFUNDED("已退款"),


    /**
     * 策略状态
     * EXECUTING 其实没有意义, 只是让策略的含义更明显
     */
    EXECUTING("执行中"),
    BUNDLED("已绑定"),


    ;

    private String explanation;

    /**
     * 订单进行中的状态
     */
    public static List<StateEnum> processing  = Arrays.asList(PENDING,PASSED, AUDITING, LENDING, LENT);

    /**
     * 放款中订单
     */
    public static List<StateEnum> pending  = Arrays.asList(PENDING,PASSED, AUDITING, LENDING);

    /**
     * 已放款, 待还款状态
     */
    public static List<StateEnum> lent  = Arrays.asList(LENT);

    /**
     * 失败订单
     */
    public static List<StateEnum> failed  = Arrays.asList(CANCELLED , REJECTED , FAILED);

    StateEnum(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
