package com.xl.canary.engine.state;

import com.xl.canary.engine.action.IActionExecutor;
import com.xl.canary.engine.event.IEvent;
import com.xl.canary.entity.IStateEntity;

/**
 * Created by gqwu on 2018/4/19.
 */
public interface IStateHandler<E extends IStateEntity> {

    E handle(E entity, IEvent event, IActionExecutor actionExecutor)
            throws Exception;

}
