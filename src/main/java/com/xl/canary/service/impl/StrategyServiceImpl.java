package com.xl.canary.service.impl;

import com.xl.canary.entity.StrategyEntity;
import com.xl.canary.enums.SubjectEnum;
import com.xl.canary.handler.condition.ConditionHandler;
import com.xl.canary.mapper.StrategyMapper;
import com.xl.canary.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 策略service
 * Created by xzhang on 2018/9/10.
 */
@Service("strategyServiceImpl")
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private ConditionHandler conditionHandler;

    @Override
    public List<StrategyEntity> listAvailableStrategiesBySubject() {
        return strategyMapper.listAvailableStrategiesBySubject(SubjectEnum.CANARY.name(), System.currentTimeMillis());
    }

    @Override
    public List<StrategyEntity> listStrategies (Map<String, Comparable> params) {
        List<StrategyEntity> strategyEntities = strategyMapper.listAvailableStrategiesBySubject(SubjectEnum.CANARY.name(), System.currentTimeMillis());
        List<StrategyEntity> passedStrategyEntities = new ArrayList<StrategyEntity>();
        for (StrategyEntity strategyEntity : strategyEntities) {
            Boolean isPass = conditionHandler.checkCondition(strategyEntity, params);
            if (isPass) {
                passedStrategyEntities.add(strategyEntity);
            }
        }
        return passedStrategyEntities;
    }
}
