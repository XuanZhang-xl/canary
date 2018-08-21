package com.xl.canary.service;

import com.xl.canary.entity.UserLevelSettingEntity;

/**
 * created by XUAN on 2018/08/20
 */
public interface UserLevelSettingService {

    /**
     * 根据等级获取entity
     * @param level
     * @return
     */
    UserLevelSettingEntity getByLevel(String level);
}
