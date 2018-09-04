package com.xl.canary.engine.event.order;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/4.
 * 发起审核
 */
public class OrderExpireEvent extends AbstractStateEvent implements IStateEvent {
    public OrderExpireEvent(String orderId) {
        super(orderId);
    }
}
