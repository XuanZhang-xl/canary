package com.xl.canary.entity;

import com.xl.canary.enums.BillTypeEnum;

/**
 * 可以抽象出Schema的类
 * Created by xzhang on 2018/9/5.
 */
public interface ISchemaEntity {

    /**
     * 获取bill的类型, schema是bill的具体实现
     * @return   bill类型
     */
    BillTypeEnum getSchemaType();
}
