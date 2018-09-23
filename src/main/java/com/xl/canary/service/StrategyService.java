package com.xl.canary.service;

import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.exception.CompareException;
import com.xl.canary.exception.NotExistException;

import java.util.List;

/**
 * Created by xzhang on 2018/9/10.
 */
public interface StrategyService {

    /**
     * 根据主体查询有效的策略
     * @return
     */
    List<StrategyEntity> listAvailableStrategiesBySubject();

    /**
     * 获取当前可用的策略
     * @param situation       需要检查的参数
     * @return
     */
    List<StrategyEntity> listStrategies (Situation situation) throws NotExistException, CompareException;
}
