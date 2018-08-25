package com.xl.canary.engine.action;

/**
 * Created by gqwu on 2018/4/4.
 * 定义一个行为
 */
public interface IActionExecutor {

    /**
     * 执行一个行为
     */
    void execute();

    /**
     * 添加一个待执行行为
     * @param action    行为
     */
    void append(IAction action);
}
