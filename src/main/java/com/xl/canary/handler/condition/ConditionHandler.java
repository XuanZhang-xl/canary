package com.xl.canary.handler.condition;

import com.xl.canary.entity.AbstractConditionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 当target是变量的时候的处理类
 * Created by xzhang on 2018/9/10.
 */
@Component("conditionHandler")
public class ConditionHandler {

    /**
     * 检查是否通过
     * @param conditions   给定限制
     * @param params       需要检查的参数
     * @return
     */
    public Boolean checkConditions (List<? extends AbstractConditionEntity> conditions, Map<String, Comparable> params) {
        return true;
    }

    /**
     * 检查是否通过
     * @param conditionEntity    给定限制
     * @param params             需要检查的参数
     * @return
     */
    public Boolean checkCondition (AbstractConditionEntity conditionEntity, Map<String, Comparable> params) {
        String condition = conditionEntity.getCondition();
        return true;
    }
}
