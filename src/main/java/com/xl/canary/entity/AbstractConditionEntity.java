package com.xl.canary.entity;

/**
 * Created by xzhang on 2018/9/10.
 */
public abstract class AbstractConditionEntity extends AbstractBaseEntity {

    /**
     * 获取限制
     * @return
     */
    public abstract String getCondition();
}
