package com.xl.canary.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gqwu on 2018/3/23.
 */
public enum StatusEnum {

    EXPIRED("已过时"),
    PENDING("排队中"),
    AUDITING("审核中"),
    PASSED("审核通过"),
    REJECTED("已拒绝"),
    LENDING("放款中"),
    FAILED("放款失败"),
    LENT("已放款"),
    PAYOFF("已还清"),
    CLEARED("已清偿"),
    CANCELLED("已取消"),
    BROKE("已破产"),

    DEDUCTING("扣款中"),
    DEDUCTED("已扣款"),
    DEDUCT_FAILED("扣款失败"),
    ENTRY_DOING("入账中, 后面会删掉"),
    ENTRY_DONE("入账完成"),
    ENTRY_FAILED("入账失败"),
    ;

    private String explanation;

    // 订单进行中的状态
    public static List<StatusEnum> processing  = Arrays.asList(PENDING,PASSED, AUDITING, LENDING, LENT);

    //放款中订单
    public static List<StatusEnum> pending  = Arrays.asList(PENDING,PASSED, AUDITING, LENDING);

    // 已放款, 待还款状态
    public static List<StatusEnum> lent  = Arrays.asList(LENT);

    //失败订单
    public static List<StatusEnum> failed  = Arrays.asList(CANCELLED , REJECTED , FAILED);

    StatusEnum(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
