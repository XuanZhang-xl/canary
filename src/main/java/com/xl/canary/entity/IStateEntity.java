package com.xl.canary.entity;

/**
 * 可以使用状态机的Entity
 * created by XUAN on 2018/08/18
 */
public interface IStateEntity {

    /**
     * 获取Entity的唯一识别id
     * @return 唯一识别id
     */
    String getUniqueId();

    /**
     * 获取Entity状态
     * @return 状态
     */
    String getState();
}
