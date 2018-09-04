package com.xl.canary.engine.event.order;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/4.
 * 用户取消
 */
public class CancelEvent extends AbstractStateEvent implements IStateEvent {
    public CancelEvent(String orderId) {
        super(orderId);
    }
}
