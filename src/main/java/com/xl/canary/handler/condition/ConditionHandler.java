package com.xl.canary.handler.condition;

import com.xl.canary.bean.dto.Situation;
import com.xl.canary.entity.AbstractConditionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 当target是变量的时候的处理类
 * Created by xzhang on 2018/9/10.
 */
@Component("conditionHandler")
public class ConditionHandler {

    /**
     * 检查是否通过
     * @param conditions   给定限制
     * @param situation    需要检查的参数
     * @return
     */
    public Boolean checkConditions (List<? extends AbstractConditionEntity> conditions, Situation situation) {
        return true;
    }

    /**
     * 检查是否通过
     * @param conditionEntity    给定限制
     * @param situation          需要检查的参数
     * @return
     */
    public Boolean checkCondition (AbstractConditionEntity conditionEntity, Situation situation) {
        String condition = conditionEntity.getCondition();
        return true;
    }

    /**
     * 列出可用的条件entity
     * @param conditions   给定限制
     * @param situation    需要检查的参数
     * @return
     */
    public List<? extends AbstractConditionEntity> listAvailableConditions (List<? extends AbstractConditionEntity> conditions, Situation situation) {
        return conditions;
    }

}
