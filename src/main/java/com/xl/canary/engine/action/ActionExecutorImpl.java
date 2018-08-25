package com.xl.canary.engine.action;

import com.xl.canary.enums.ExecuteActionMethodEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gqwu on 2018/4/5.
 *
 * 避免不同actionExecutor之间相互干扰，
 * 所以，需要使用者新建executor实例，但建议使用同一个线程池进行任务执行
 */

public class ActionExecutorImpl implements IActionExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ActionExecutorImpl.class);

    private List<IAction> actions = new ArrayList<IAction>();

    private ThreadPoolTaskExecutor executor;

    public ActionExecutorImpl (ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute() {
        Iterator<IAction> iterator = actions.iterator();
        while (iterator.hasNext()) {
            IAction action = iterator.next();
            String actionType = action.getActionType();
            logger.info("开始执行订单的id为[{}], 类型为[{}]的[{}], 当前任务队列长度[{}]", action.getUniqueId(), actionType, action.getActionName(), actions.size());
            if (ExecuteActionMethodEnum.CONTINUE.name().equals(actionType)) {
                action.run();
            } else if (ExecuteActionMethodEnum.NEW_THREAD.name().equals(actionType)) {
                executor.execute(action);
            } else {
                logger.error("订单的id为[{}], 类型为[{}]的[{}], 无法匹配类型, 忽略", action.getUniqueId(), actionType, action.getActionName());
            }
            iterator.remove();
        }
    }

    @Override
    public void append(IAction action) {
        this.actions.add(action);
    }
}
