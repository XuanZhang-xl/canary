package com.xl.canary.engine.event.order.pay;

import com.xl.canary.engine.event.AbstractStateEvent;
import com.xl.canary.engine.event.IStateEvent;

/**
 * Created by gqwu on 2018/4/8.
 */
public class DeductLaunchEvent extends AbstractStateEvent implements IStateEvent {

    public DeductLaunchEvent(String payOrderId) {
        super(payOrderId);
    }
}
