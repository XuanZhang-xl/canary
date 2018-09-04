package com.xl.canary.engine.event.order;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/4.
 * 恢复订单
 */
public class RecoveringEvent extends AbstractStateEvent implements IStateEvent {
    public RecoveringEvent(String orderId) {
        super(orderId);
    }
}
