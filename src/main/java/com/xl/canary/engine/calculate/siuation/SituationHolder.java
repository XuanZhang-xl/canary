package com.xl.canary.engine.calculate.siuation;

import javax.naming.InitialContext;

/**
 * 同一个线程, 应只有一个Situation
 * created by XUAN on 2018/09/22
 */
public class SituationHolder {

    /**
     * 线程局部变量，只为该线程服务，保障线程安全
     */
    private static final ThreadLocal<Situation> SITUATION_HOLDER = new ThreadLocal<Situation>() {
        // 初始化Situation
        @Override
        protected Situation initialValue() {
            return new Situation();
        }
    };

    /**
     * 设置situation
     *
     * @param situation
     */
    public static void setSituation(Situation situation) {
        SITUATION_HOLDER.set(situation);
    }

    /**
     * 获取situation
     *
     * @return
     */
    public static Situation getSituation() {
        return SITUATION_HOLDER.get();
    }

    /**
     * 清除threadLocal
     */
    public static void clear() {
        SITUATION_HOLDER.remove();
    }
}
