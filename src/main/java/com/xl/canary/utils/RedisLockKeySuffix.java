package com.xl.canary.utils;

/**
 * redis锁使用的key的后缀, 保证在不该出现一样的key的地方不出现一样的key
 * Created by xzhang on 2018/7/9.
 */
public interface RedisLockKeySuffix {

    /**
     * 下单锁
     */
    String LOAN_ORDER_KEY = "LOAN_ORDER_KEY_";

    /**
     * 还款锁
     */
    String PAY_ORDER_KEY = "PAY_ORDER_KEY_";

}
