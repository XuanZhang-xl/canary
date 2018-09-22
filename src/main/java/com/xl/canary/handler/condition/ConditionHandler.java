package com.xl.canary.handler.condition;

import com.alibaba.fastjson.JSONObject;
import com.xl.canary.bean.dto.ConditionDescription;
import com.xl.canary.engine.calculate.siuation.Situation;
import com.xl.canary.entity.AbstractConditionEntity;
import com.xl.canary.exception.CompareException;
import com.xl.canary.exception.NotExistException;
import com.xl.canary.handler.aoperator.ArithmeticOperatorHandlerFactory;
import com.xl.canary.handler.aoperator.IArithmeticOperatorHandler;
import com.xl.canary.utils.ConditionUtils;
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
    public Boolean checkCondition (AbstractConditionEntity conditionEntity, Situation situation) throws NotExistException, CompareException {
        String conditionStr = conditionEntity.getCondition();
        // 替换目标限制中的占位符
        conditionStr = ConditionUtils.replacePlaceholder(conditionStr, situation);
        List<ConditionDescription> conditionDescriptions = JSONObject.parseArray(conditionStr, ConditionDescription.class);

        for (ConditionDescription conditionDescription : conditionDescriptions) {
            String currentParam = situation.get(conditionDescription.getCondition());
            if (currentParam == null) {
                throw new NotExistException("检查限制条件, 缺少参数 " + conditionDescription.getCondition().name());
            }
            IArithmeticOperatorHandler operatorHandler = ArithmeticOperatorHandlerFactory.instance(conditionDescription.getOperator());
            if (!operatorHandler.operate(conditionDescription.getStandardParam(), conditionDescription.getCurrentParam())) {
                return false;
            }
        }
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
