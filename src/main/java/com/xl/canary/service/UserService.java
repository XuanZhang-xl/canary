package com.xl.canary.service;

import com.xl.canary.entity.UserEntity;
import org.springframework.stereotype.Service;

/**
 * created by XUAN on 2018/08/20
 */
@Service("userService")
public interface UserService {

    /**
     * 根据userCode获取用户
     * @param userCode
     * @return
     */
    UserEntity getByUserCode(String userCode);
}
