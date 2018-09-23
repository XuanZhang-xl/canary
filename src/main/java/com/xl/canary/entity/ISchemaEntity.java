package com.xl.canary.entity;

import com.xl.canary.enums.SchemaTypeEnum;

/**
 * 可以抽象出Schema的类
 * Created by xzhang on 2018/9/5.
 */
public interface ISchemaEntity {

    /**
     * 获取Schema的类型
     * @return   Schema类型
     */
    SchemaTypeEnum getSchemaType();
}
