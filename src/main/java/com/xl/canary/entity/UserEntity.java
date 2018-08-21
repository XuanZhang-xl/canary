package com.xl.canary.entity;

import javax.persistence.Table;

/**
 * 用户表
 * created by XUAN on 2018/08/20
 */
@Table(name = "t_canary_user")
public class UserEntity extends AbstractBaseEntity{

    /**
     * 用户唯一编号
     */
    private String userCode;

    /**
     * 名字
     */
    private String nickName;

    /**
     * 等级
     */
    private String level;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
