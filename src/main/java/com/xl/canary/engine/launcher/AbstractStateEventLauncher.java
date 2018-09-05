package com.xl.canary.engine.launcher;

import com.xl.canary.engine.action.ActionExecutorImpl;
import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IStateEvent;
import com.xl.canary.engine.state.IStateHandler;
import com.xl.canary.engine.state.StateHandlerFactory;
import com.xl.canary.entity.IStateEntity;
import com.xl.canary.exception.InvalidEventException;
import com.xl.canary.exception.LockException;
import com.xl.canary.exception.NotExistException;
import com.xl.canary.lock.Lock;
import com.xl.canary.lock.RedisService;
import com.xl.canary.utils.IDWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * Created by gqwu on 2018/4/19.
 * 状态机事件处理器抽象类，用于处理特定的，作用于包含状态字段的实体类的事件，
 * 要求该实体类的状态处理，实现@code IStateHandler接口
 */
@Component
public abstract class AbstractStateEventLauncher<E extends IStateEntity, V extends IStateEvent> implements IEventLauncher<V> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStateEventLauncher.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private IDWorker idWorker;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private StateHandlerFactory stateHandlerFactory;

    /**
     * 根据唯一字段获得实体
     * @param uniqueId
     * @return
     */
    public abstract E selectEntity (String uniqueId);

    /**
     * 保存更改后的实体
     * @param entity
     */
    public abstract void saveEntity (E entity);

    @Override
    public void launch(V event) throws Exception {
        String uniqueId = event.getUniqueId();
        if (StringUtils.isBlank(uniqueId)) {
            throw new InvalidEventException("事件传递的状态实体ID为空，事件：" + event.toString());
        }
        Lock lock = new Lock(uniqueId, String.valueOf(idWorker.generateID()));
        IActionExecutor actionExecutor = null;
        try {
            /**
             * 非阻塞锁，不等待竞争者释放锁，因为状态机事件，一般是互斥，
             * 当两个非互斥事件发生时，业务层需要主动捕捉LockException，重新发送事件
             * 事务必须在锁释放前提交
             */
            if (redisService.lock(lock.getName(), lock.getValue(), 5000, TimeUnit.MILLISECONDS)) {
                actionExecutor = ((AbstractStateEventLauncher) AopContext.currentProxy()).stateMachineHandle(uniqueId, event);
            } else {
                throw new LockException("事件为目标状态实体请求锁时，竞争失败，事件：" + event.toString() + "，实体：" + uniqueId);
            }
        } finally {
            redisService.release(lock);
        }

        /**
         * 完成事件处理后，由线程池异步执行附加操作
         */
        actionExecutor.execute();
    }

    @Transactional(rollbackFor = Exception.class)
    public IActionExecutor stateMachineHandle (String uniqueId, V event) throws Exception {
        IActionExecutor actionExecutor = new ActionExecutorImpl(executor);
        E entity = selectEntity(uniqueId);
        if (entity == null) {
            throw new NotExistException("事件的目标状态实体不存在，事件：" + event.toString() + "，实体：" + uniqueId);
        }
        IStateHandler stateHandler = stateHandlerFactory.instance(entity.getState(), entity.getClass());

        if (stateHandler == null) {
            throw new NotExistException("事件目标状态实体状态没有合适的处理器，事件：" + event.toString() +"订单号: " + uniqueId + "，订单状态：" + entity.getState());
        }
        IStateEntity stateEntity = stateHandler.handle(entity, event, actionExecutor);
        this.saveEntity((E) stateEntity);
        return actionExecutor;
    }

}
