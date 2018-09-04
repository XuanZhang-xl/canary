package com.xl.canary.enums;

/**
 * 优先级
 * priority越大优先级越高,
 * 如果优惠券优先级高的与优先级低的有某一字段的重复, 则优先级高的会覆盖优先级低的
 * 如果优惠券的优先级一样, 视为冲突, 不可一起使用
 * created by XUAN on 2018/09/02
 */
public enum PriorityEnum {

    /**
     * 最优先
     */
    PRIVILEGE(Integer.MAX_VALUE),

    /**
     * 优先
     */
    PRIVILEGE_TEN(10010),
    PRIVILEGE_NINE(10009),
    PRIVILEGE_EIGHT(10008),
    PRIVILEGE_SEVEN(10007),
    PRIVILEGE_SIX(10006),
    PRIVILEGE_FIVE(10005),
    PRIVILEGE_FOUR(10004),
    PRIVILEGE_THREE(10003),
    PRIVILEGE_TWO(10002),
    PRIVILEGE_ONE(10001),
    PRIVILEGE_ZERO(10000),

    /**
     * 一般
     */
    GENERAL_TEN(10),
    GENERAL_NINE(9),
    GENERAL_EIGHT(8),
    GENERAL_SEVEN(7),
    GENERAL_SIX(6),
    GENERAL_FIVE(5),
    GENERAL_FOUR(4),
    GENERAL_THREE(3),
    GENERAL_TWO(2),
    GENERAL_ONE(1),
    GENERAL_ZERO(0),

    /**
     * 末尾
     */
    LAST_TEN(-9989),
    LAST_NINE(-9990),
    LAST_EIGHT(-9991),
    LAST_SEVEN(-9992),
    LAST_SIX(-9993),
    LAST_FIVE(-9994),
    LAST_FOUR(-9995),
    LAST_THREE(-9996),
    LAST_TWO(-9997),
    LAST_ONE(-9998),
    LAST_ZERO(-9999),

    /**
     * 最末尾
     */
    LAST(Integer.MIN_VALUE),

    ;
    private Integer priority;


    PriorityEnum(Integer priority) {
        this.priority = priority;
    }
}
