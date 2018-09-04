package com.xl.canary.engine.action;

import com.xl.canary.enums.ExecuteActionMethodEnum;

/**
 * Created by gqwu on 2018/4/4.
 * 定义一个行为
 */
public interface IAction extends Runnable{

    /**
     * 获取action的名字
     * @return
     */
    String getActionName();

    /**
     * 获取行为类型
     * @return
     */
    String getActionType();

    /**
     * 获取唯一编号, TODO: 是否所有Action都有这个属性?
     * @return
     */
    String getUniqueId();

    /**
     * 执行逻辑
     */
    @Override
    void run();
}
