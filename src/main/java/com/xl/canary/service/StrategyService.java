package com.xl.canary.service;

import com.xl.canary.bean.dto.Situation;
import com.xl.canary.entity.AbstractConditionEntity;
import com.xl.canary.entity.StrategyEntity;

import java.util.List;
import java.util.Map;

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
    List<StrategyEntity> listStrategies (Situation situation);
}
