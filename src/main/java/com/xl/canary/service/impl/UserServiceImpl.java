package com.xl.canary.service.impl;

import com.xl.canary.entity.UserEntity;
import com.xl.canary.mapper.UserMapper;
import com.xl.canary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by XUAN on 2018/08/20
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserEntity getByUserCode(String userCode) {
        UserEntity user = new UserEntity();
        user.setUserCode(userCode);
        user.setIsDeleted(0);
        return userMapper.selectOne(user);
    }
}
