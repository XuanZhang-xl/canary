package com.xl.canary.service.impl;

import com.xl.canary.entity.UserLevelSettingEntity;
import com.xl.canary.mapper.UserLevelSettingMapper;
import com.xl.canary.service.UserLevelSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by XUAN on 2018/08/20
 */
@Service("userLevelSettingService")
public class UserLevelSettingServiceImpl implements UserLevelSettingService {

    @Autowired
    private UserLevelSettingMapper userLevelSettingMapper;

    @Override
    public UserLevelSettingEntity getByLevel(String level) {
        UserLevelSettingEntity userLevelSetting = new UserLevelSettingEntity();
        userLevelSetting.setLevel(level);
        userLevelSetting.setIsDeleted(0);
        return userLevelSettingMapper.selectOne(userLevelSetting);
    }
}
