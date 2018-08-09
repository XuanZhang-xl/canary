package com.xl.canary.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by gqwu on 2017/6/21.
 * 实体类基类
 */
public abstract class AbstractBaseEntity implements IEntity, Serializable {

    /**
     * 数据库生成id
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 数据记录创建时间
     */
    private Long createTime;

    /**
     * 数据记录更新时间
     */
    private Long updateTime;

    /**
     * 是否软删除, 0: 没删除, 1: 已删除
     */
    private Integer isDeleted;

    /**
     * 说明
     */
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
